package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

/**
 * <p>ComponentAdapter interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface ComponentAdapter {

	/**
	 * <p>createAdapter.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.config.IConfig} object.
	 * @return a {@link de.mhus.lib.form.UiComponent} object.
	 */
	UiComponent createAdapter(IConfig config);
	
	/**
	 * <p>getDefinition.</p>
	 *
	 * @return a {@link de.mhus.lib.form.ComponentDefinition} object.
	 */
	ComponentDefinition getDefinition();
	
}
