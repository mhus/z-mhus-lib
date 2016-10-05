package de.mhus.lib.core.matcher;

import java.util.Map;

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

	@Override
	protected boolean matches(Map<String, ?> map) {
		for (ModelPart part : components) {
			if (!part.m(map)) return false;
		}
		return true;
	}

}
