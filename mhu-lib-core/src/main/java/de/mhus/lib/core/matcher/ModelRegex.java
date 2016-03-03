package de.mhus.lib.core.matcher;

import java.util.regex.Pattern;

/**
 * <p>ModelRegex class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ModelRegex extends ModelPattern {

	private Pattern pattern; 

	/** {@inheritDoc} */
	@Override
	public boolean matches(String str) {
		return pattern.matcher(str).matches();
	}

	/** {@inheritDoc} */
	@Override
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	/** {@inheritDoc} */
	@Override
	public String getPattern() {
		return pattern.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String getPatternTypeName() {
		return "regex";
	}
	
}
