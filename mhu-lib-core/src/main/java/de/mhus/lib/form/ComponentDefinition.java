package de.mhus.lib.form;

import de.mhus.lib.core.config.IConfig;

/**
 * <p>ComponentDefinition class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class ComponentDefinition {

	private boolean fullSizeComponent;
	private String wizzard;
	private IConfig configurationForm;

	/**
	 * <p>isFullSizeComonent.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFullSizeComonent() {
		return fullSizeComponent;
	}
	
	/**
	 * <p>Getter for the field <code>wizzard</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getWizzard() {
		return wizzard;
	}
	
	/**
	 * <p>Getter for the field <code>configurationForm</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public IConfig getConfigurationForm() {
		return configurationForm;
	}
	
}
