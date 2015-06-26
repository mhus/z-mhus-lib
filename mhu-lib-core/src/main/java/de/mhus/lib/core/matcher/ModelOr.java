package de.mhus.lib.core.matcher;

public class ModelOr extends ModelComposit {

	@Override
	public boolean matches(String str) {
		for (ModelPart part : components) {
			if (part.m(str)) return true;
		}
		return false;
	}

	@Override
	public String getOperatorName() {
		return "or";
	}

}
