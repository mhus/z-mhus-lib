package de.mhus.lib.core.matcher;

public abstract class ModelPattern extends ModelPart {

	public abstract void setPattern(String pattern);
	public abstract String getPattern();
	public abstract String getPatternTypeName();

	@Override
	public String toString() {
		return (getParamName() != null ? "$" + getParamName() + "$ " : "" ) + (isNot() ? "not " : "") + getPatternTypeName() + " '" + getPattern() + "'";
	}

}
