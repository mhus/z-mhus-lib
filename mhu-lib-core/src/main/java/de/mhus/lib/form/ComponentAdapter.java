package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

public interface ComponentAdapter {

	UiComponent createAdapter(IConfig config);
	
	ComponentDefinition getDefinition();
	
}
