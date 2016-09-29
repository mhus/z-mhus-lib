package de.mhus.lib.core.matcher;

import java.util.Map;

/**
 * <p>ModelAnd class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ModelAnd extends ModelComposit {

	/** {@inheritDoc} */
	@Override
	public boolean matches(String str) {
		for (ModelPart part : components) {
			if (!part.m(str)) return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public String getOperatorName() {
		return "and";
	}

	/** {@inheritDoc} */
	@Override
	protected boolean matches(Map<String, ?> map) {
		for (ModelPart part : components) {
			if (!part.m(map)) return false;
		}
		return true;
	}

}
