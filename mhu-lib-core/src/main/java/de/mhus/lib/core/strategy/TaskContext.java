package de.mhus.lib.core.strategy;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.config.IConfig;

/**
 * <p>TaskContext interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface TaskContext extends Monitor {

	/**
	 * <p>getConfig.</p>
	 *
	 * @return a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	IConfig getConfig();
	/**
	 * <p>isTestOnly.</p>
	 *
	 * @return a boolean.
	 */
	boolean isTestOnly();
	
	/**
	 * <p>getParameters.</p>
	 *
	 * @return a {@link de.mhus.lib.core.IProperties} object.
	 */
	IProperties getParameters();
	
	/**
	 * <p>addErrorMessage.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 */
	void addErrorMessage(String msg);
	/**
	 * <p>getErrorMessage.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getErrorMessage();
	
}
