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
package de.mhus.lib.core.operation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.basics.Versioned;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.core.util.Nls;
import de.mhus.lib.core.util.ParameterDefinitions;
import de.mhus.lib.core.util.Version;
import de.mhus.lib.form.ModelUtil;

public class OperationDescription implements MNlsProvider, Nls, Versioned, Externalizable {

    public static final String TAGS = "tags";

    public static final String TAG_TECH = "tech";
    public static final String TECH_JAVA = "java";

    private static Log log = Log.getLog(OperationDescription.class);

    private String title;
    private DefRoot form;
    private IProperties labels;

    private ParameterDefinitions parameterDef;

    private MNls nls;
    private MNlsProvider nlsProvider;

    private Version version;
    private UUID uuid;

    private String path;

    public OperationDescription() {}

    public OperationDescription(
            UUID uuid, Class<?> clazz, MNlsProvider nlsProvider, String version, String title) {
        this(uuid, clazz, nlsProvider, null, title, null, null);
    }

    public OperationDescription(
            UUID uuid, Class<?> clazz, MNlsProvider nlsProvider, Version version, String title) {
        this(uuid, clazz, nlsProvider, version, title, null, null);
    }

    public OperationDescription(UUID uuid, Class<?> clazz, MNlsProvider nlsProvider, String title) {
        this(uuid, clazz, nlsProvider, null, title, null, null);
    }

    public OperationDescription(Operation owner, Version version, String title, DefRoot form) {
        this(owner.getUuid(), owner.getClass(), owner, version, title, null, form);
    }

    public OperationDescription(Operation owner, String title, DefRoot form) {
        this(owner.getUuid(), owner.getClass(), owner, null, title, null, form);
    }

    public OperationDescription(Operation owner, String path, String title, DefRoot form, String ... labels ) {
        this(owner.getUuid(), path, null, owner, title, IProperties.explodeToMProperties(labels), form);
    }

    public OperationDescription(UUID uuid, Class<?> clazz, MNlsProvider nlsProvider, Version version, String title, DefRoot form) {
        this(uuid, clazz, nlsProvider, version, title, null, form);
    }

    public OperationDescription(
            UUID uuid,
            Class<?> clazz,
            MNlsProvider nlsProvider,
            Version version,
            String title,
            IProperties labels,
            DefRoot form) {
        this(uuid, clazz.getCanonicalName(), version, nlsProvider, title, labels, form);
    }

    protected void setForm(DefRoot form) {
        if (form == null) {
            this.form = null;
            parameterDef = null;
            return;
        }
        try {
            form = form.build();
            parameterDef = ParameterDefinitions.create(form);
            //			Document document = MXml.createDocument();
            //			Element de = document.createElement("root");
            //			XmlConfig c = new XmlConfig(de);
            //			new ConfigBuilder().cloneConfig(form, c);
            //			String formStr = MXml.toString(de, false);
            this.form = form;
        } catch (Exception e) {
            log.w("invalid form", path, version, e);
        }
    }

    public ParameterDefinitions getParameterDefinitions() {
        return parameterDef;
    }

    public OperationDescription(
            UUID uuid, String path, Version version, String title, DefRoot form, String ... labels) {
        this(uuid, path, version, null, title, IProperties.explodeToMProperties(labels), form);
    }
    
    public OperationDescription(
            UUID uuid, String path, Version version, MNlsProvider nlsProvider, String title) {
        this(uuid, path, version, nlsProvider, title, null, null);
    }

    public OperationDescription(
            UUID uuid, String path, Version version, String title, IProperties labels, DefRoot form) {
        this(uuid, path, version, null, title, labels, form);
    }
    
    /**
     * Create a clone with the same UUID, but it's possible to manipulate the labels.
     *
     * @param uuid
     * @param path
     * @param version
     * @param nlsProvider
     * @param title
     * @param labels 
     * @param form
     */
    public OperationDescription(
            UUID uuid,
            String path,
            Version version,
            MNlsProvider nlsProvider,
            String title,
            IProperties labels,
            DefRoot form) {
        this.uuid = uuid;
        this.path = path;
        this.nlsProvider = nlsProvider;
        this.title = title;
        if (version == null) version = Version.V_0_0_0;
        this.labels = labels;
        this.version = version;
        if (form != null) setForm(form);
    }

    public OperationDescription(OperationDescription desc) {
        this(
                desc.getUuid(),
                desc.getPath(),
                desc.getVersion(),
                desc.nlsProvider,
                desc.getTitle(),
                desc.hasLabels() ? new MProperties( desc.getLabels()) : null,
                desc.getForm());
// done by setForm() if (desc.parameterDef != null) this.parameterDef = desc.parameterDef;
    }

    public String getTitle() {
        return title;
    }

    public DefRoot getForm() {
        return form;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String getVersionString() {
        return version.toString();
    }

    public Version getVersion() {
        return version;
    }

    public boolean hasLabels() {
    	return labels != null;
    }
    
    public IReadProperties getLabels() {
        if (this.labels == null) this.labels = new MProperties();
        return labels;
    }

//    public void setLabels(IProperties labels) {
//        this.labels = labels;
//    }

//    public OperationDescription putLabel(String key, String value) {
//        if (this.labels == null) this.labels = new MProperties();
//        this.labels.put(key, value);
//        return this;
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof OperationDescription) {
            OperationDescription od = (OperationDescription) o;
            return MSystem.equals(path, od.path) && MSystem.equals(version, od.version);
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return MSystem.toString(this, uuid, path, version, labels);
    }

    @Override
    public MNls getNls() {
        if (nls == null) nls = nlsProvider.getNls();
        return nls;
    }

    @Override
    public String nls(String text) {
        return MNls.find(this, text);
    }

    public String getCaption() {
        return nls("caption=" + getTitle());
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(1);
        out.writeObject(title);
        out.writeObject(form);
        out.writeObject(labels);
        out.writeObject(parameterDef);
        out.writeObject(version);
        out.writeObject(path);
        out.writeObject(uuid);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readInt(); // 1
        title = (String) in.readObject();
        form = (DefRoot) in.readObject();
        labels = (MProperties) in.readObject();
        parameterDef = (ParameterDefinitions) in.readObject();
        version = (Version) in.readObject();
        path = (String) in.readObject();
        path = (String) in.readObject();
        uuid = (UUID) in.readObject();
    }

    public ObjectNode toJson() throws Exception {
        ObjectNode json = MJson.createObjectNode();

        json.put("path", getPath());
        json.put("uuid", getUuid().toString());
        json.put("title", getTitle());
        json.put("version", getVersionString());
        DefRoot form = getForm();
        if (form != null) {
            form.build();
            ObjectNode formJson = ModelUtil.toJson(form);
            json.set("form", formJson);
        }
        ObjectNode labelsJson = MJson.createObjectNode();
        for (Entry<String, Object> label : getLabels().entrySet()) {
            labelsJson.put(label.getKey(), MCast.toString(label.getValue()));
        }
        json.set("labels", labelsJson);

        return json;
    }

    public static OperationDescription fromJson(JsonNode json) {
        UUID uuid = UUID.fromString(json.get("uuid").asText());
        String path = json.get("path").asText();
        String title = json.get("title").asText();
        Version version = new Version(json.get("version").asText());

        OperationDescription desc = new OperationDescription(uuid, path, version, null, title);

        if (json.has("form")) {
            DefRoot form = ModelUtil.fromJson((ObjectNode) json.get("form"));
            desc.setForm(form);
        }

        IReadProperties labels = desc.getLabels();
        for (Map.Entry<String, JsonNode> field : M.iterate(json.get("labels").fields())) {
            ((MProperties)labels).put(field.getKey(), field.getValue().asText());
        }
        return desc;
    }

	public String getPathVersion() {
		return getPath() + ":" + getVersionString();
	}
}
