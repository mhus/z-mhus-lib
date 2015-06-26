package de.mhus.lib.core.matcher;

public abstract class ModelPart {
	
	private boolean not;
	
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
	
	protected abstract boolean matches(String str);
	
}
