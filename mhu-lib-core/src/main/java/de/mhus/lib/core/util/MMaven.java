package de.mhus.lib.core.util;

import java.io.File;
import java.io.FileOutputStream;
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
import de.mhus.lib.core.parser.AbstractStringPropertyReplacer;

public class MMaven {
	
	private static CfgString CFG_MAVEN_LOCAL_REPO = new CfgString(MMaven.class, "repositoryLocaltion", null);
	private static String repositoryLocation;
	private static Log log = Log.getLog(MMaven.class);
	
	private static AbstractStringPropertyReplacer replacer = new AbstractStringPropertyReplacer() {
		
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
	public static File locateArtifact(String groupId, String artifactId, String version, String type) {
		getLocalRepositoryLocation();
		if(type == null) type = "jar";
		
		String artifactLocation = groupId.replace('.', '/') + '/' + artifactId + '/' + version + '/' + artifactId + '-' + version + '.' + type;
		
		return new File(repositoryLocation + '/' + artifactLocation);
	}

	/**
	 * Return elements first from local settings then from global settings. So most relevant 
	 * ones are at the beginning.
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
		
		repositoryLocation =  CFG_MAVEN_LOCAL_REPO.value();

		if (repositoryLocation == null) {
			// from settings
			try {
				List<Element> list = getConfigElements("localRepository");
				if (list.size() > 0)
					repositoryLocation = MXml.getValue(list.get(0), false);
			} catch (Throwable t) {}
		}
		
		if (repositoryLocation == null) {
			// default location
			repositoryLocation =  "${user.home}/.m2/repository";
		}
		
		if (repositoryLocation != null) {
			repositoryLocation = replacer.process(repositoryLocation);
		}
		
		return repositoryLocation;
	}
	
	/**
	 * Downloading an artifact from repositories. This will not implement the full specification.
	 * @param groupId
	 * @param artifactId
	 * @param version
	 * @param type
	 * @return true if download was successful
	 */
	public static boolean downloadArtefact(String groupId, String artifactId, String version, String type) {
		// get mirrors
		List<Element> mirrors = getConfigElements("mirrors/mirror");
		// get Proxies
		List<Element> proxies = getConfigElements("proxies/proxy");
		// get repositories
		List<Element> repos = getConfigElements("repositories/repository");
		
		// add default repo at the end
		try {
			Document doc = MXml.loadXml("<repository><id>central</id><name>Central Repository</name><url>http://repo.maven.apache.org/maven2</url></repository>");
			repos.add(doc.getDocumentElement());
		} catch (Throwable t) {
			log.e(t);
		}

		boolean isRelease = version.indexOf("SNAPSHOT") < 0;
		
		// check for each repo
		for (Element repo : repos) {
			String id = MXml.getValue(repo, "id", "");

			boolean hasReleases = MCast.toboolean(MXml.getValue(repo, "releases/enabled","true"), false);
			boolean hasStapshots = MCast.toboolean(MXml.getValue(repo, "snapshots/enabled","true"), false);
			
			if (isRelease && !hasReleases || !isRelease && !hasStapshots)
				continue;
			
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
						if (rule.startsWith("!") && rule.substring(1).equals(id))
							ok = false;
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
				//find proxy
				for (Element proxy : proxies) {
					if (!MXml.getValue(proxy, "active", "").equals("true")) 
						continue;
					if (protocol.equals(MXml.getValue(proxy, "protocol", ""))) {
						useProxy = proxy;
						useProxyId = MXml.getValue(useProxy, "host", "");
						break;
					}
				}
			}

			log.t("try download artifact:",groupId,artifactId,version,type,"source:",id,useProxyId,useMirrorId);
			
			try {
				MHttpClientBuilder client = new MHttpClientBuilder();
				if (useProxy != null) {
					client.setProxyHost(MXml.getValue(useProxy, "host", ""));
					client.setProxyPort(MCast.toint(MXml.getValue(useProxy, "port", "3128"), 3128));
				} else
					client.setUseSystemProperties(true);

				String download = url + '/' + groupId.replace('.', '/') + '/' + artifactId + '/' + version + artifactId + '-' + version + '.' + type;
				HttpGet action = new HttpGet(download);
				HttpResponse response = client.execute(action);
				
				if (response.getStatusLine().getStatusCode() == 200) {
					log.d("download artifact:",groupId,artifactId,version,type,"source:",id,useProxyId,useMirrorId);
					InputStream is = response.getEntity().getContent();
					File target = locateArtifact(groupId, artifactId, version, type);
					FileOutputStream os = new FileOutputStream(target);
					MFile.copyFile(is, os);
					os.close();
					is.close();
				
					if (!type.equals("pom")) {
						// download pom also
						download = url + '/' + groupId.replace('.', '/') + '/' + artifactId + '/' + version + artifactId + '-' + version + ".pom";
						
						action = new HttpGet(download);
						response = client.execute(action);
						
						if (response.getStatusLine().getStatusCode() == 200) {
							log.t("download pom:",groupId,artifactId,version,"pom","source:",id,useProxyId,useMirrorId);
							is = response.getEntity().getContent();
							target = locateArtifact(groupId, artifactId, version, "pom");
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

	
}
