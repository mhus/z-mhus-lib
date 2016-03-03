package de.mhus.lib.core.matcher;

/**
 * <p>ModelOr class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
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

}
