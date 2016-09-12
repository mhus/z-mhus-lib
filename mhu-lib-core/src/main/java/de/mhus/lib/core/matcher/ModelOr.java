package de.mhus.lib.core.matcher;

import java.util.Map;

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

	@Override
	protected boolean matches(Map<String, ?> map) {
		for (ModelPart part : components) {
			if (part.m(map)) return true;
		}
		return false;
	}

}
