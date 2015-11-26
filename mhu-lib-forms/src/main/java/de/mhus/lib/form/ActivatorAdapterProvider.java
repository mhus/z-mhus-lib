package de.mhus.lib.form;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.IConfig;

public class ActivatorAdapterProvider implements ComponentAdapterProvider {

	protected MActivator activator;

	public ActivatorAdapterProvider(MActivator activator) {
		this.activator = activator;
	}

	@Override
	public UiComponent createComponent(String id, IConfig config) throws Exception {
		return getAdapter(id).createAdapter(config);
	}

	@Override
	public ComponentAdapter getAdapter(String id) throws Exception {
		return (ComponentAdapter) activator.getObject(id);
	}

	@Override
	public UiWizard createWizard(String obj) throws Exception {
		return (UiWizard) activator.createObject(obj);
	}

	public MActivator getActivator() {
		return activator;
	}

}
