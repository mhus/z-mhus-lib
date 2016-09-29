package de.mhus.lib.core.matcher;

import de.mhus.lib.core.MString;

public class ModelFs extends ModelPattern {

	private String pattern;

	@Override
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	protected boolean matches(String str) {
		return MString.compareFsLikePattern(str, pattern);
	}

	@Override
	public String getPattern() {
		return pattern;
	}

	@Override
	public String getPatternTypeName() {
		return "fs";
	}

}
