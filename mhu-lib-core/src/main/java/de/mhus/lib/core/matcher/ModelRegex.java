package de.mhus.lib.core.matcher;

import java.util.regex.Pattern;

public class ModelRegex extends ModelPattern {

	private Pattern pattern; 

	@Override
	public boolean matches(String str) {
		return pattern.matcher(str).matches();
	}

	@Override
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public String getPattern() {
		return pattern.toString();
	}

	@Override
	public String getPatternTypeName() {
		return "regex";
	}
	
}
