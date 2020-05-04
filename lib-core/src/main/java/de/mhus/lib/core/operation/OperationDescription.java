/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.operation;

import java.util.HashMap;
import java.util.UUID;

import de.mhus.lib.basics.Versioned;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.core.util.Nls;
import de.mhus.lib.core.util.ParameterDefinitions;
import de.mhus.lib.core.util.Version;

public class OperationDescription implements MNlsProvider, Nls, Versioned {

    public static final String TAGS = "tags";

    public static final String TAG_TECH = "tech";
    public static final String TECH_JAVA = "java";

    private static Log log = Log.getLog(OperationDescription.class);

    private String id;
    private String title;
    private String group;
    private DefRoot form;
    private HashMap<String, String> parameters;

    private ParameterDefinitions parameterDef;

    private MNls nls;
    private MNlsProvider nlsProvider;

    private Version version;
    private UUID uuid;

    public OperationDescription() {}

    public OperationDescription(
            UUID uuid, Class<?> clazz, MNlsProvider nlsProvider, String version, String title) {
        this(uuid, clazz, nlsProvider, null, title, null);
    }

    public OperationDescription(
            UUID uuid, Class<?> clazz, MNlsProvider nlsProvider, Version version, String title) {
        this(uuid, clazz, nlsProvider, version, title, null);
    }

    public OperationDescription(UUID uuid, Class<?> clazz, MNlsProvider nlsProvider, String title) {
        this(uuid, clazz, nlsProvider, null, title, null);
    }

    public OperationDescription(Operation owner, Version version, String title, DefRoot form) {
        this(owner.getUuid(), owner.getClass(), owner, version, title, form);
    }

    public OperationDescription(Operation owner, String title, DefRoot form) {
        this(owner.getUuid(), owner.getClass(), owner, null, title, form);
    }

    public OperationDescription(
            Operation owner, String group, String id, String title, DefRoot form) {
        this(owner.getUuid(), group, id, null, owner, title, form);
    }

    public OperationDescription(
            UUID uuid,
            Class<?> clazz,
            MNlsProvider nlsProvider,
            Version version,
            String title,
            DefRoot form) {
        this(
                uuid,
                clazz.getPackage().getName(),
                clazz.getSimpleName(),
                version,
                nlsProvider,
                title,
                form);
    }

    public void setForm(DefRoot form) {
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
            log.w("invalid form", group, id, version, e);
        }
    }

    public ParameterDefinitions getParameterDefinitions() {
        return parameterDef;
    }

    public OperationDescription(
            UUID uuid,
            OperationGroupDescription group,
            String id,
            Version version,
            MNlsProvider nlsProvider,
            String title) {
        this(uuid, group.getGroup(), id, version, nlsProvider, title, null);
    }

    public OperationDescription(
            UUID uuid,
            String group,
            String id,
            Version version,
            MNlsProvider nlsProvider,
            String title) {
        this(uuid, group, id, version, nlsProvider, title, null);
    }

    public OperationDescription(
            UUID uuid,
            String group,
            String id,
            Version version,
            MNlsProvider nlsProvider,
            String title,
            DefRoot form) {
        this.uuid = uuid;
        this.id = id;
        this.group = group;
        this.nlsProvider = nlsProvider;
        this.title = title;
        if (version == null) version = Version.V_0_0_0;
        this.version = version;
        if (form != null) setForm(form);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGroup() {
        return group;
    }

    public DefRoot getForm() {
        return form;
    }

    public String getPath() {
        return group + '.' + id;
    }

    @Override
    public String getVersionString() {
        return version.toString();
    }

    public Version getVersion() {
        return version;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public OperationDescription putParameter(String key, String value) {
        if (this.parameters == null) this.parameters = new HashMap<>();
        this.parameters.put(key, value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof OperationDescription) {
            OperationDescription od = (OperationDescription) o;
            return MSystem.equals(group, od.group) && MSystem.equals(id, od.id);
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return MSystem.toString(this, group, id, parameters);
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
}
