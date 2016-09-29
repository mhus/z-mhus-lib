package de.mhus.lib.core.matcher;

import java.util.Map;

/**
 * <p>ModelOr class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ModelOr extends ModelComposit {

	/** {@inheritDoc} */
	@Override
	public boolean matches(String str) {
		for (ModelPart part : components) {
			if (part.m(str)) return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String getOperatorName() {
		return "or";
	}

	/** {@inheritDoc} */
	@Override
	protected boolean matches(Map<String, ?> map) {
		for (ModelPart part : components) {
			if (part.m(map)) return true;
		}
		return false;
	}

}
