package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.control.Wizard;

/**
 * <p>FmWizard class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class FmWizard extends DefAttribute {

	private DefAttribute[] options;

	/**
	 * <p>Constructor for FmWizard.</p>
	 *
	 * @param handler a {@link java.lang.String} object.
	 * @param options a {@link de.mhus.lib.core.definition.DefAttribute} object.
	 */
	public FmWizard(String handler, DefAttribute ... options) {
		super("wizard", handler);
		this.options = options;
	}
	
	/**
	 * <p>Constructor for FmWizard.</p>
	 *
	 * @param handler a {@link java.lang.Class} object.
	 * @param options a {@link de.mhus.lib.core.definition.DefAttribute} object.
	 */
	public FmWizard(Class<? extends Wizard> handler, DefAttribute ... options) {
		super("wizard", handler.getCanonicalName());
		this.options = options;
	}

	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent parent) throws MException {
		super.inject(parent);
		if (options != null) {
			DefComponent dummy = new DefComponent("wizard", options);
			parent.setConfig("wizard", dummy);
			dummy.inject(null);
		}
	}
	
}
