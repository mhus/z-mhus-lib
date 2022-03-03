/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.io.http.MHttpClientBuilder;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.parser.StringPropertyReplacer;
import de.mhus.lib.errors.UsageException;

public class MMaven {

    private static CfgString CFG_MAVEN_LOCAL_REPO =
            new CfgString(MMaven.class, "repositoryLocaltion", null);
    private static String repositoryLocation;
    private static Log log = Log.getLog(MMaven.class);

    private static StringPropertyReplacer replacer =
            new StringPropertyReplacer() {

                @Override
                public String findValueFor(String key) {
                    if (key.equals("user.home")) {
                        return MSystem.getUserHome().getAbsolutePath();
                    }
                    if (key.startsWith("env.")) {
                        return System.getenv(key.substring(4));
                    }
                    return null;
                }
            };

    // https://www.codenotfound.com/maven-change-location-local-repository.html
    public static File locateArtifact(Artifact artifact) {
        getLocalRepositoryLocation();

        String artifactLocation = artifact.toLocation();

        return new File(repositoryLocation + '/' + MFile.normalizePath(artifactLocation));
    }

    /**
     * Return elements first from local settings then from global settings. So most relevant ones
     * are at the beginning.
     *
     * @param path
     * @return List of found elements
     */
    public static List<Element> getConfigElements(String path) {
        LinkedList<Element> out = new LinkedList<>();

        try {
            File settingsFile = new File(MSystem.getUserHome() + "/.m2/settings.xml");
            if (settingsFile.exists() && settingsFile.isFile()) {
                Document settings = MXml.loadXml(settingsFile);
                MXml.getElementsByPath(settings.getDocumentElement(), path, out);
            }
        } catch (Throwable t) {
            log.d(t);
        }

        if (MString.isSet(System.getenv("MAVEN_HOME"))) {
            try {
                File settingsFile = new File(System.getenv("MAVEN_HOME") + "/conf/settings.xml");
                if (settingsFile.exists() && settingsFile.isFile()) {
                    Document settings = MXml.loadXml(settingsFile);
                    MXml.getElementsByPath(settings.getDocumentElement(), path, out);
                }
            } catch (Throwable t) {
                log.d(t);
            }
        }
        return out;
    }

    public static String getLocalRepositoryLocation() {

        if (repositoryLocation != null) return repositoryLocation;

        repositoryLocation = CFG_MAVEN_LOCAL_REPO.value();

        if (repositoryLocation == null) {
            // from settings
            try {
                List<Element> list = getConfigElements("localRepository");
                if (list.size() > 0) repositoryLocation = MXml.getValue(list.get(0), false);
            } catch (Throwable t) {
            }
        }

        if (repositoryLocation == null) {
            // default location
            repositoryLocation = "${user.home}/.m2/repository";
        }

        if (repositoryLocation != null) {
            repositoryLocation = replacer.process(repositoryLocation);
        }

        return repositoryLocation;
    }

    /**
     * Downloading an artifact from repositories. This will not implement the full specification.
     *
     * @param artifact
     * @return true if download was successful
     */
    public static boolean downloadArtefact(Artifact artifact) {
        // get mirrors
        List<Element> mirrors = getConfigElements("mirrors/mirror");
        // get Proxies
        List<Element> proxies = getConfigElements("proxies/proxy");
        // get repositories
        List<Element> repos = getConfigElements("repositories/repository");

        // add default repo at the end
        try {
            Document doc =
                    MXml.loadXml(
                            "<repository><id>central</id><name>Central Repository</name><url>http://repo.maven.apache.org/maven2</url></repository>");
            repos.add(doc.getDocumentElement());
        } catch (Throwable t) {
            log.e(t);
        }

        boolean isRelease = artifact.isRelease();

        // check for each repo
        for (Element repo : repos) {
            String id = MXml.getValue(repo, "id", "");

            boolean hasReleases =
                    MCast.toboolean(MXml.getValue(repo, "releases/enabled", "true"), false);
            boolean hasStapshots =
                    MCast.toboolean(MXml.getValue(repo, "snapshots/enabled", "true"), false);

            if (isRelease && !hasReleases || !isRelease && !hasStapshots) continue;

            String url = MXml.getValue(repo, "url", null);
            if (url == null) continue;
            String protocol = MString.beforeIndex(url, ':');

            Element useProxy = null;
            Element useMirror = null;
            String useProxyId = null;
            String useMirrorId = null;

            if (!protocol.equals("file")) {
                // find mirror
                for (Element mirror : mirrors) {
                    String[] rules = MXml.getValue(mirror, "mirrorOf", "").split(",");
                    boolean ok = false;
                    for (String rule : rules) {
                        if (rule.startsWith("!") && rule.substring(1).equals(id)) ok = false;
                        else
                            ok = (rule.equals("*") || rule.equals("external:*") || rule.equals(id));
                    }
                    if (ok) {
                        useMirror = mirror;
                        useMirrorId = MXml.getValue(useMirror, "id", "");
                        url = MXml.getValue(useMirror, "url", null);
                        protocol = MString.beforeIndex(url, ':');
                        break;
                    }
                }
                // find proxy
                for (Element proxy : proxies) {
                    if (!MXml.getValue(proxy, "active", "").equals("true")) continue;
                    if (protocol.equals(MXml.getValue(proxy, "protocol", ""))) {
                        useProxy = proxy;
                        useProxyId = MXml.getValue(useProxy, "host", "");
                        break;
                    }
                }
            }

            log.t("try download artifact:", artifact, "source:", id, useProxyId, useMirrorId);

            try {
                MHttpClientBuilder client = new MHttpClientBuilder();
                if (useProxy != null) {
                    client.setProxyHost(MXml.getValue(useProxy, "host", ""));
                    client.setProxyPort(MCast.toint(MXml.getValue(useProxy, "port", "3128"), 3128));
                } else client.setUseSystemProperties(true);

                String download = url + '/' + artifact.toLocation();
                HttpGet action = new HttpGet(download);
                HttpResponse response = client.getHttpClient().execute(action);

                if (response.getStatusLine().getStatusCode() == 200) {
                    log.d("download artifact:", artifact, "source:", id, useProxyId, useMirrorId);
                    InputStream is = response.getEntity().getContent();
                    File target = locateArtifact(artifact);
                    if (!target.getParentFile().exists())
                        if (!target.getParentFile().mkdirs())
                            throw new IOException("can't create directory: " + target);
                    FileOutputStream os = new FileOutputStream(target);
                    MFile.copyFile(is, os);
                    os.close();
                    is.close();

                    if (!artifact.getType().equals("pom")) {
                        // download pom also
                        Artifact pomArtifact = artifact.createPomArtifact();
                        download = url + '/' + pomArtifact.toLocation();

                        action = new HttpGet(download);
                        response = client.getHttpClient().execute(action);

                        if (response.getStatusLine().getStatusCode() == 200) {
                            log.t(
                                    "download pom:",
                                    artifact,
                                    "pom",
                                    "source:",
                                    id,
                                    useProxyId,
                                    useMirrorId);
                            is = response.getEntity().getContent();
                            target = locateArtifact(pomArtifact);
                            os = new FileOutputStream(target);
                            MFile.copyFile(is, os);
                            os.close();
                            is.close();
                        }
                    }
                    client.close();
                    return true;
                }

                client.close();

            } catch (Throwable t) {
                log.d("download failed", url, useProxyId, t);
            }
        }

        return false;
    }

    public static class Artifact {
        private String groupId;
        private String artifactId;
        private String version;
        private String type;

        private Artifact(String groupId, String artifactId, String version, String type) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            if (type == null) type = "jar";
            this.type = type;
        }

        public Artifact(Element xml) {
            if ("project".equals(xml.getNodeName())) {
                Element parent = MXml.getElementByPath(xml, "parent");
                if (parent != null) {
                    groupId = MXml.getValue(parent, "groupId", groupId);
                    version = MXml.getValue(parent, "version", version);
                }
            }
            artifactId = MXml.getValue(xml, "artifactId", artifactId);
            groupId = MXml.getValue(xml, "groupId", groupId);
            version = MXml.getValue(xml, "version", version);
            type = MXml.getValue(xml, "packaging", type);
            if (type == null) type = MXml.getValue(xml, "type", null);
            if (type == null) type = "";
            if (type.equals("bundle")) type = "jar";
        }

        public Artifact createPomArtifact() {
            if (type.equals("pom")) return this;
            return new Artifact(groupId, artifactId, version, "pom");
        }

        public String toLocation() {
            return groupId.replace('.', '/')
                    + '/'
                    + artifactId
                    + '/'
                    + version
                    + '/'
                    + artifactId
                    + '-'
                    + version
                    + "."
                    + type;
        }

        public boolean isRelease() {
            return version.indexOf("SNAPSHOT") < 0;
        }

        public String getGroupId() {
            return groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public String getVersion() {
            return version;
        }

        public String getType() {
            return type;
        }

        public Version getEstimatedVersion() {
            if (version == null) return Version.V_0_0_0;
            return new Version(version);
        }

        @Override
        public String toString() {
            return groupId + "/" + artifactId + "/" + version + "/" + type;
        }
    }

    public static Artifact toArtifact(
            String groupId, String artifactId, String version, String type) {
        return new Artifact(groupId, artifactId, version, type);
    }

    public static Artifact toArtifact(String def) {
        if (def == null) return null;
        if (def.startsWith("mvn:")) {
            String[] parts = def.substring(4).split("/");
            return new Artifact(parts[0], parts[1], parts[2], parts.length > 3 ? parts[3] : null);
        }
        if (def.startsWith("wrap:")) {
            int p = def.indexOf('$');
            if (p > 0) def = def.substring(0, p);
            String[] parts = def.substring(5).split("/");
            return new Artifact(parts[0], parts[1], parts[2], parts.length > 3 ? parts[3] : null);
        }
        if (def.indexOf('/') > -1) {
            String[] parts = def.split("/");
            return new Artifact(parts[0], parts[1], parts[2], parts.length > 3 ? parts[3] : null);
        }
        if (def.indexOf(':') > -1) {
            String[] parts = def.split(":");
            return new Artifact(parts[0], parts[1], parts[3], parts[2]);
        }
        throw new UsageException("Unknown format");
    }

    public static List<Artifact> findLocalArtifacts(String groupId) {
        getLocalRepositoryLocation();

        File start =
                new File(repositoryLocation + "/" + MFile.normalizePath(groupId.replace('.', '/')));
        List<File> pomList =
                MFile.findAllFiles(
                        start,
                        new FileFilter() {

                            @Override
                            public boolean accept(File file) {
                                return (file.isDirectory()
                                        || file.isFile() && file.getName().endsWith(".pom"));
                            }
                        });

        LinkedList<Artifact> out = new LinkedList<>();
        for (File pomFile : pomList) {
            try {
                Document pom = MXml.loadXml(pomFile);
                Artifact artifact = new Artifact(pom.getDocumentElement());
                out.add(artifact);
            } catch (Throwable t) {

            }
        }

        return out;
    }

    public static boolean deleteLocalArtifact(Artifact artifact) {
        File loc = locateArtifact(artifact);
        if (!loc.exists()) return false;
        if (loc.isFile()) {
            loc = loc.getParentFile();
        }
        if (loc.isDirectory()) {
            MFile.deleteDir(loc);
            return true;
        }
        return false;
    }

    public static Pom loadPom(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) throw new FileNotFoundException(fileName);
        return new Pom(file);
    }
}
