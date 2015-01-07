package de.mhus.lib.core.parser;

import java.util.Map;

import de.mhus.lib.core.MString;

/**
 * A default implementation to parse and hold a constant string value.
 * 
 * @author mikehummel
 *
 */
public abstract class ConstantParsingPart extends StringParsingPart {

	protected StringBuffer buffer;
	protected String content;

	@Override
	public void execute(StringBuffer out, Map<String, Object> attributes) {
		out.append(content);
	}

	@Override
	public void doPreParse() {
		buffer = new StringBuffer();
	}

	@Override
	public void doPostParse() {
		content = buffer.toString();
		buffer = null;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public void dump(int level, StringBuffer out) {
		MString.appendRepeating(level, ' ', out);
		out.append(getClass().getCanonicalName()).append(" ").append(content).append("\n");
	}

}
