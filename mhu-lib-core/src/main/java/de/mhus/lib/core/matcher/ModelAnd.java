package de.mhus.lib.core.matcher;

/**
 * <p>ModelAnd class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
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

}
