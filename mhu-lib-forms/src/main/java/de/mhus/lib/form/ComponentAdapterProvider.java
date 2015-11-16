package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

public interface ComponentAdapterProvider {

	UiComponent createComponent(IConfig config);

	ComponentAdapter getAdapter(String id);

	UiWizard createWizard(String obj);

}
