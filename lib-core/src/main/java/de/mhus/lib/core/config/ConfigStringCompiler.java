/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.config;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.DefaultScriptPart;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.core.parser.StringPart;
import de.mhus.lib.errors.MException;

public class ConfigStringCompiler extends StringCompiler {

    private MConfig rootConfig;

    ConfigStringCompiler(MConfig rootConfig) {
        this.rootConfig = rootConfig;
    }

    @Override
    protected StringPart createDefaultAttributePart(String part) {
        if (part.startsWith(">root:")) return new RootAttributePart(part);
        if (part.startsWith(">js:")) return new DefaultScriptPart(part);
        return new ConfigAttributePart(part);
    }

    private class RootAttributePart implements StringPart {
        private String name;
        private String def;
        private IConfig config;

        public RootAttributePart(String part) {
            name = MString.afterIndex(part, ':');
            config = rootConfig;
            while (config.getParent() != null && config.getParent() != config)
                config = config.getParent();
            int pos = name.indexOf(',');
            if (pos > 0) {
                def = name.substring(pos + 1);
                name = name.substring(0, pos);
            }
        }

        @Override
        public void execute(StringBuilder out, Map<String, Object> attributes) throws MException {
            out.append(config.getString(name, def));
        }

        @Override
        public void dump(int level, StringBuilder out) {
            MString.appendRepeating(level, ' ', out);
            out.append(getClass().getCanonicalName())
                    .append(name)
                    .append("(")
                    .append(def)
                    .append(")");
        }
    }

    private class ConfigAttributePart implements StringPart {

        private String name;
        private String def;
        private MConfig config;

        public ConfigAttributePart(String part) {
            name = part;
            int pos = name.indexOf(',');
            if (pos > 0) {
                def = name.substring(pos + 1);
                name = name.substring(0, pos);
            }
            config = rootConfig;
            if (name.startsWith("/")) {
                while (config.getParent() != null) config = (MConfig) config.getParent();
                name = name.substring(1);
            } else
                while (name.startsWith("../")) {
                    config = (MConfig) config.getParent();
                    name = name.substring(3);
                    if (config == null) break;
                }
        }

        @Override
        public void execute(StringBuilder out, Map<String, Object> attributes) throws MException {
            int level = 0;
            if (attributes != null && attributes instanceof ConfigMap) {
                level = ((ConfigMap) attributes).getLevel();
            }
            if (config == null) out.append(def);
            else out.append(config.getExtracted(name, def, level));
        }

        @Override
        public void dump(int level, StringBuilder out) {
            MString.appendRepeating(level, ' ', out);
            out.append(getClass().getCanonicalName())
                    .append(name)
                    .append("(")
                    .append(def)
                    .append(")");
        }
    }

    static class ConfigMap implements Map<String, Object> {

        private int level;
        private MConfig config;

        ConfigMap(int level) {
            this.level = level;
        }

        ConfigMap(int level, MConfig config) {
            this.level = level;
            this.config = config;
        }

        @Override
        public int size() {
            return config == null ? 0 : config.size();
        }

        public int getLevel() {
            return level;
        }

        @Override
        public boolean isEmpty() {
            return config == null ? true : config.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return config == null ? false : config.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return config == null ? false : config.containsValue(value);
        }

        @Override
        public Object get(Object key) {
            return config == null ? null : config.get(key);
        }

        @Override
        public Object put(String key, Object value) {
            return null;
        }

        @Override
        public Object remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> m) {}

        @Override
        public void clear() {}

        @Override
        public Set<String> keySet() {
            return config == null ? null : config.keySet();
        }

        @Override
        public Collection<Object> values() {
            return null;
        }

        @Override
        public Set<java.util.Map.Entry<String, Object>> entrySet() {
            return null;
        }
    }
}
