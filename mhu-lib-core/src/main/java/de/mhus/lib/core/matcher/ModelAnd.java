package de.mhus.lib.core.matcher;

public class ModelAnd extends ModelComposit {

	@Override
	public boolean matches(String str) {
		for (ModelPart part : components) {
			if (!part.m(str)) return false;
		}
		return true;
	}

	@Override
	public String getOperatorName() {
		return "and";
	}

}
