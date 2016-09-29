package de.mhus.lib.core.matcher;

import java.util.Map;

/**
 * <p>Abstract ModelPattern class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class ModelPattern extends ModelPart {

	/**
	 * <p>setPattern.</p>
	 *
	 * @param pattern a {@link java.lang.String} object.
	 */
	public abstract void setPattern(String pattern);
	/**
	 * <p>getPattern.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getPattern();
	/**
	 * <p>getPatternTypeName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getPatternTypeName();

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return (getParamName() != null ? "$" + getParamName() + "$ " : "" ) + (isNot() ? "not " : "") + getPatternTypeName() + " '" + getPattern() + "'";
	}

}
