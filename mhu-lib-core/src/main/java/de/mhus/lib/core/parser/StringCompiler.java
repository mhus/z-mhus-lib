/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.parser;

import java.util.LinkedList;
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
		String[] parts = MString.split(in, separator);
		boolean c = true;
		for (String part : parts) {
			if (c) {
				compiled.add(createContentPart(part));
				c = false;
			} else {
				if (part.length()==0) {
					compiled.add(createContentPart(separator));					
				} else {
					compiled.add(createAttributePart(part));					
				}
				c = true;
			}
		}
		return new CompiledString(compiled);
	}

	protected StringPart createAttributePart(String part) {
		if (part.startsWith("#env."))
			return new EnvironmentPart(part);
		if (part.startsWith("#system."))
			return new SystemPart(part);
		if (part.equals("#hostname"))
		    return new StaticPart(MSystem.getHostname());
		if (part.equals("#username"))
		    return new StaticPart(MSystem.getUsername());
        if (part.equals("#userhome"))
            return new StaticPart(MSystem.getUserHome().getAbsolutePath());
		return createDefaultAttributePart(part);
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

	
	public class EnvironmentPart implements StringPart {

		private String name;
		private String def;

		public EnvironmentPart(String part) {
			name = MString.afterIndex(part,'.');
			int p = name.indexOf(':');
			if (p >= 0) {
				def = name.substring(p+1);
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
			out.append(getClass().getCanonicalName()).append(" ").append(name).append(':').append(def).append('\n');
		}

	}
	
    public class StaticPart implements StringPart {

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
	public class SystemPart implements StringPart {

		private String name;
		private String def;

		public SystemPart(String part) {
			name = MString.afterIndex(part,'.');
			int p = name.indexOf(':');
			if (p >= 0) {
				def = name.substring(p+1);
				name = name.substring(0, p);
			}
		}

		@Override
		public void execute(StringBuilder out, Map<String, Object> attributes) {
			out.append(System.getProperty(name,def));
		}
		
		@Override
		public void dump(int level, StringBuilder out) {
			MString.appendRepeating(level, ' ', out);
			out.append(getClass().getCanonicalName()).append(" ").append(name).append(':').append(def).append("\n");
		}

	}
	
	public class AttributePart implements StringPart {

		private String name;
		private String def;

		public AttributePart(String part) {
			name = part;
			int p = name.indexOf(':');
			if (p >= 0) {
				def = name.substring(p+1);
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
			out.append(getClass().getCanonicalName()).append(" ").append(name).append(':').append(def).append("\n");
		}
		
	}
	
}
