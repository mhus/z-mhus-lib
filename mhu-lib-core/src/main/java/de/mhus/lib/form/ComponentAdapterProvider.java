package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

public interface ComponentAdapterProvider {

	UiComponent createComponent(String id, IConfig config) throws Exception;

	ComponentAdapter getAdapter(String id) throws Exception;

	UiWizard createWizard(String obj) throws Exception;

}
