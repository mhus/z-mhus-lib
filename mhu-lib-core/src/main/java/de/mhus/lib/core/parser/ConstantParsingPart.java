package de.mhus.lib.core.parser;

import java.util.Map;

import de.mhus.lib.core.MString;

/**
 * A default implementation to parse and hold a constant string value.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class ConstantParsingPart extends StringParsingPart {

	protected StringBuffer buffer;
	protected String content;

	/** {@inheritDoc} */
	@Override
	public void execute(StringBuffer out, Map<String, Object> attributes) {
		out.append(content);
	}

	/** {@inheritDoc} */
	@Override
	public void doPreParse() {
		buffer = new StringBuffer();
	}

	/** {@inheritDoc} */
	@Override
	public void doPostParse() {
		content = buffer.toString();
		buffer = null;
	}

	/**
	 * <p>Getter for the field <code>content</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContent() {
		return content;
	}
	
	/** {@inheritDoc} */
	@Override
	public void dump(int level, StringBuffer out) {
		MString.appendRepeating(level, ' ', out);
		out.append(getClass().getCanonicalName()).append(" ").append(content).append("\n");
	}

}
