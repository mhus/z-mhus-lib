package de.mhus.lib.core.matcher;

import java.util.Map;

public class NullPattern extends ModelPattern {

	@Override
	public void setPattern(String pattern) {
		
	}

	@Override
	public String getPattern() {
		return "null";
	}

	@Override
	public String getPatternTypeName() {
		return "null";
	}

	@Override
	protected boolean matches(String str) {
		return str == null;
	}

	protected boolean matches(Map<String,Object> map) {
		Object val = map.get(getParamName());
		return val == null;
	}

}
