package de.mhus.lib.core.matcher;

import java.util.LinkedList;
import java.util.Map;

public abstract class ModelComposit extends ModelPart {

	protected LinkedList<ModelPart> components = new LinkedList<>();
	
	public void add(ModelPart part) {
		components.add(part);
	}
	
	public int size() {
		return components.size();
	}

	protected abstract boolean matches(Map<String,?> map);

	@Override
	public String toString() {
		return getOperatorName() + (isNot() ? " not" : "") + components;
	}
	
	public abstract String getOperatorName();
	
}
