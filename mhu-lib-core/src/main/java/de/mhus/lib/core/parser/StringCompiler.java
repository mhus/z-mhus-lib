package de.mhus.lib.core.parser;

import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.core.MString;

public class StringCompiler implements Parser {

	private static StringCompiler defaultCompiler = new StringCompiler();

	protected String separator = "$";

	public static CompiledString compile(String in) {
		return compile(in, defaultCompiler);
	}
	
	public static CompiledString compile(String in, StringCompiler compiler) {
		return compiler.compileString(in);
	}

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
		public void execute(StringBuffer out, Map<String, Object> attributes) {
			out.append(content);
		}
		
		@Override
		public void dump(int level, StringBuffer out) {
			MString.appendRepeating(level, ' ', out);
			out.append(getClass().getCanonicalName()).append(" ").append(content).append("\n");
		}

	}

	
	public class EnvironmentPart implements StringPart {

		private String name;

		public EnvironmentPart(String part) {
			name = MString.afterIndex(part,'.');
		}

		@Override
		public void execute(StringBuffer out, Map<String, Object> attributes) {
			out.append(System.getenv(name));
		}
		
		@Override
		public void dump(int level, StringBuffer out) {
			MString.appendRepeating(level, ' ', out);
			out.append(getClass().getCanonicalName()).append(" ").append(name);
		}

	}
	
	public class SystemPart implements StringPart {

		private String name;

		public SystemPart(String part) {
			name = MString.afterIndex(part,'.');
		}

		@Override
		public void execute(StringBuffer out, Map<String, Object> attributes) {
			out.append(System.getProperty(name));
		}
		
		@Override
		public void dump(int level, StringBuffer out) {
			MString.appendRepeating(level, ' ', out);
			out.append(getClass().getCanonicalName()).append(" ").append(name).append("\n");
		}

	}
	
	public class AttributePart implements StringPart {

		private String name;

		public AttributePart(String part) {
			name = part;
		}

		@Override
		public void execute(StringBuffer out, Map<String, Object> attributes) {
			if (attributes != null) out.append(attributes.get(name));
		}
		
		@Override
		public void dump(int level, StringBuffer out) {
			MString.appendRepeating(level, ' ', out);
			out.append(getClass().getCanonicalName()).append(" ").append(name).append("\n");
		}
		
	}
	
}
