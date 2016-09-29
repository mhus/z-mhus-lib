package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

/**
 * <p>ComponentAdapterProvider interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface ComponentAdapterProvider {

	/**
	 * <p>createComponent.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @param config a {@link de.mhus.lib.core.config.IConfig} object.
	 * @return a {@link de.mhus.lib.form.UiComponent} object.
	 * @throws java.lang.Exception if any.
	 */
	UiComponent createComponent(String id, IConfig config) throws Exception;

	/**
	 * <p>getAdapter.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.form.ComponentAdapter} object.
	 * @throws java.lang.Exception if any.
	 */
	ComponentAdapter getAdapter(String id) throws Exception;

	/**
	 * <p>createWizard.</p>
	 *
	 * @param obj a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.form.UiWizard} object.
	 * @throws java.lang.Exception if any.
	 */
	UiWizard createWizard(String obj) throws Exception;

}
