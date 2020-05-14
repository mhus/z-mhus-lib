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
package de.mhus.lib.core.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.MException;

public class StringCompiler implements Parser {

    private static StringCompiler defaultCompiler = new StringCompiler();

    protected String separator = "$";

    public static CompiledString compile(String in) {
        return compile(in, defaultCompiler);
    }

    public static CompiledString compile(String in, StringCompiler compiler) {
        return compiler.compileString(in);
    }

    @Override
    public CompiledString compileString(String in) {
        LinkedList<StringPart> compiled = new LinkedList<StringPart>();
        List<String> parts = splitString(in);
        boolean c = true;
        for (String part : parts) {
            if (c) {
                compiled.add(createContentPart(part));
                c = false;
            } else {
                if (part.length() == 0) {
                    compiled.add(createContentPart(separator));
                } else {
                    compiled.add(createAttributePart(part));
                }
                c = true;
            }
        }
        return new CompiledString(compiled);
    }

    // supports:
    // $name$
    // ${name}
    protected List<String> splitString(String in) {
        LinkedList<String> parts = new LinkedList<>();
        while (true) {
            if (in.length() == 0) return parts;
            int nextPos = in.indexOf(separator);
            if (nextPos < 0) {
                parts.add(in);
                return parts;
            }

            parts.add(in.substring(0, nextPos)); // add content part

            if (nextPos == in.length() - separator.length()) { // at the end
                parts.add(separator);
                return parts;
            }
            if (in.charAt(nextPos + separator.length()) == '{') {
                int endPos = in.indexOf('}', nextPos);
                if (endPos < 0) { // not found
                    parts.add(in.substring(nextPos));
                    return parts;
                }
                parts.add(in.substring(nextPos + separator.length() + 1, endPos));
                in = in.substring(endPos + 1);
            } else {
                int endPos = in.indexOf(separator, nextPos + separator.length());
                if (endPos < 0) { // not found
                    parts.add(in.substring(nextPos));
                    return parts;
                }
                parts.add(in.substring(nextPos + separator.length(), endPos));
                in = in.substring(endPos + separator.length());
            }
        }
    }

    protected StringPart createAttributePart(String part) {
        if (part.startsWith("#"))
            return createExtraAttributePart(part);
        return createDefaultAttributePart(part);
    }

    public static StringPart createExtraAttributePart(String part) {
        if (part.startsWith("#env.")) return new EnvironmentPart(part);
        if (part.startsWith("#system.")) return new SystemPart(part);
        if (part.equals("#hostname")) return new StaticPart(MSystem.getHostname());
        if (part.equals("#username")) return new StaticPart(MSystem.getUsername());
        if (part.equals("#userhome"))
            return new StaticPart(MSystem.getUserHome().getAbsolutePath());
        return new StaticPart(part.substring(1));
    }

    protected StringPart createDefaultAttributePart(String part) {
        return new AttributePart(part);
    }

    protected StringPart createContentPart(String part) {
        return new ContentPart(part);
    }

    public class ContentPart implements StringPart {

        private String content;

        public ContentPart(String part) {
            content = part;
        }

        @Override
        public void execute(StringBuilder out, Map<String, Object> attributes) {
            out.append(content);
        }

        @Override
        public void dump(int level, StringBuilder out) {
            MString.appendRepeating(level, ' ', out);
            out.append(getClass().getCanonicalName()).append(" ").append(content).append("\n");
        }
    }

    public static class EnvironmentPart implements StringPart {

        private String name;
        private String def;

        public EnvironmentPart(String part) {
            name = MString.afterIndex(part, '.');
            int p = name.indexOf(':');
            if (p >= 0) {
                def = name.substring(p + 1);
                name = name.substring(0, p);
            }
        }

        @Override
        public void execute(StringBuilder out, Map<String, Object> attributes) {
            String v = System.getenv(name);
            if (v == null) v = def;
            out.append(v);
        }

        @Override
        public void dump(int level, StringBuilder out) {
            MString.appendRepeating(level, ' ', out);
            out.append(getClass().getCanonicalName())
                    .append(" ")
                    .append(name)
                    .append(':')
                    .append(def)
                    .append('\n');
        }
    }

    public static class StaticPart implements StringPart {

        private String value;

        public StaticPart(String value) {
            this.value = value;
        }

        @Override
        public void execute(StringBuilder out, Map<String, Object> attributes) throws MException {
            out.append(value);
        }

        @Override
        public void dump(int level, StringBuilder out) {
            MString.appendRepeating(level, ' ', out);
            out.append(getClass().getCanonicalName()).append(" '").append(value).append("'\n");
        }
    }

    public static class SystemPart implements StringPart {

        private String name;
        private String def;

        public SystemPart(String part) {
            name = MString.afterIndex(part, '.');
            int p = name.indexOf(':');
            if (p >= 0) {
                def = name.substring(p + 1);
                name = name.substring(0, p);
            }
        }

        @Override
        public void execute(StringBuilder out, Map<String, Object> attributes) {
            out.append(System.getProperty(name, def));
        }

        @Override
        public void dump(int level, StringBuilder out) {
            MString.appendRepeating(level, ' ', out);
            out.append(getClass().getCanonicalName())
                    .append(" ")
                    .append(name)
                    .append(':')
                    .append(def)
                    .append("\n");
        }
    }

    public class AttributePart implements StringPart {

        private String name;
        private String def;

        public AttributePart(String part) {
            name = part;
            int p = name.indexOf(':');
            if (p >= 0) {
                def = name.substring(p + 1);
                name = name.substring(0, p);
            }
        }

        @Override
        public void execute(StringBuilder out, Map<String, Object> attributes) {
            if (attributes != null) out.append(attributes.getOrDefault(name, def));
        }

        @Override
        public void dump(int level, StringBuilder out) {
            MString.appendRepeating(level, ' ', out);
            out.append(getClass().getCanonicalName())
                    .append(" ")
                    .append(name)
                    .append(':')
                    .append(def)
                    .append("\n");
        }
    }
}
