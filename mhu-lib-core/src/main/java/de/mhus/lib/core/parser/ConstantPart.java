package de.mhus.lib.core.parser;

import java.util.Map;

import de.mhus.lib.core.MString;

/**
 * <p>ConstantPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ConstantPart implements ParsingPart {

	private String content;

	/**
	 * <p>Constructor for ConstantPart.</p>
	 *
	 * @param content a {@link java.lang.String} object.
	 */
	public ConstantPart(String content) {
		this.content = content;
	}
	
	/** {@inheritDoc} */
	@Override
	public void execute(StringBuffer out, Map<String, Object> attributes) {
		out.append(content);
	}

	/** {@inheritDoc} */
	@Override
	public void parse(ParseReader str) throws ParseException {
	}

	/** {@inheritDoc} */
	@Override
	public void dump(int level, StringBuffer out) {
		MString.appendRepeating(level, ' ', out);
		out.append(getClass().getCanonicalName()).append(content);
	}


}
