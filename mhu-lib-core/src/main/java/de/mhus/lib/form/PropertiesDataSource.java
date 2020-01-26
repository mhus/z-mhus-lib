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
package de.mhus.lib.form;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;

public class PropertiesDataSource implements DataSource {

    private MProperties properties;

    public MProperties getProperties() {
        return properties;
    }

    public void setProperties(MProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean getBoolean(UiComponent component, String name, boolean def) {
        return properties.getBoolean(toName(component.getName(), name), def);
    }

    @Override
    public int getInt(UiComponent component, String name, int def) {
        return properties.getInt(toName(component.getName(), name), def);
    }

    @Override
    public String getString(UiComponent component, String name, String def) {
        return properties.getString(toName(component.getName(), name), def);
    }

    @Override
    public Object getObject(UiComponent component, String name, Object def) {
        Object val = properties.getProperty(toName(component.getName(), name));
        if (val == null) return def;
        return val;
    }

    @Override
    public Object getObject(String name, Object def) {
        Object val = properties.getProperty(name);
        if (val == null) return def;
        return val;
    }

    @Override
    public void setObject(UiComponent component, String name, Object value) {
        properties.put(toName(component.getName(), name), value);
    }

    private String toName(String component, String name) {
        if (MString.isEmpty(name)) return component;
        return component + "." + name;
    }

    @Override
    public DataSource getNext() {
        return null;
    }
}
