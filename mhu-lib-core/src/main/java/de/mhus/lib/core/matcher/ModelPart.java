package de.mhus.lib.core.matcher;

import java.util.Map;

public abstract class ModelPart {
	
	private boolean not;
	private String param;
	
	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}
	
	public boolean m(String str) {
		if (not)
			return !matches(str);
		else
			return matches(str);
	}

	public boolean m(Map<String,Object> map) {
		if (not)
			return !matches(map);
		else
			return matches(map);
	}

	public void setParamName(String param) {
		this.param = param;
	}
	
	public String getParamName() {
		return param;
	}

	protected boolean matches(Map<String,Object> map) {
		Object val = map.get(param);
		if (val != null)
			return matches(String.valueOf(val));
		return false;
	}
	
	protected abstract boolean matches(String str);
	
}
