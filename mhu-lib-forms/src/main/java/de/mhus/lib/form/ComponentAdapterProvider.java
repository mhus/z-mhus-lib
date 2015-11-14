package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

public interface ComponentAdapterProvider {

	UiComponent createAdapter(IConfig config);

	ComponentAdapter getAdapter(String id);

}
