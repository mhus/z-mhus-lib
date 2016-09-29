package de.mhus.lib.form;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.IConfig;

/**
 * <p>ActivatorAdapterProvider class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class ActivatorAdapterProvider implements ComponentAdapterProvider {

	protected MActivator activator;

	/**
	 * <p>Constructor for ActivatorAdapterProvider.</p>
	 *
	 * @param activator a {@link de.mhus.lib.core.MActivator} object.
	 */
	public ActivatorAdapterProvider(MActivator activator) {
		this.activator = activator;
	}

	/** {@inheritDoc} */
	@Override
	public UiComponent createComponent(String id, IConfig config) throws Exception {
		return getAdapter(id).createAdapter(config);
	}

	/** {@inheritDoc} */
	@Override
	public ComponentAdapter getAdapter(String id) throws Exception {
		return (ComponentAdapter) activator.getObject(id);
	}

	/** {@inheritDoc} */
	@Override
	public UiWizard createWizard(String obj) throws Exception {
		return (UiWizard) activator.createObject(obj);
	}

	/**
	 * <p>Getter for the field <code>activator</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MActivator} object.
	 */
	public MActivator getActivator() {
		return activator;
	}

}
