package de.mhus.lib.core.strategy;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.config.IConfig;

/**
 * <p>DefaultTaskContext class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class DefaultTaskContext extends DefaultMonitor implements TaskContext {

	protected IConfig config;
	protected boolean test;
	protected IProperties parameters;
	protected String errorMessage;

	/**
	 * <p>setTestOnly.</p>
	 *
	 * @param test a boolean.
	 */
	public void setTestOnly(boolean test) {
		this.test = test;
	}

	/**
	 * <p>Setter for the field <code>config</code>.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public void setConfig(IConfig config) {
		this.config = config;
	}

	/**
	 * <p>Setter for the field <code>parameters</code>.</p>
	 *
	 * @param parameters a {@link de.mhus.lib.core.IProperties} object.
	 */
	public void setParameters(IProperties parameters) {
		this.parameters = parameters;
	}

	/** {@inheritDoc} */
	@Override
	public IConfig getConfig() {
		return config;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTestOnly() {
		return test;
	}

	/** {@inheritDoc} */
	@Override
	public IProperties getParameters() {
		return parameters;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void addErrorMessage(String msg) {
		if (msg == null) return;
		if (errorMessage == null)
			errorMessage = msg;
		else
			errorMessage = errorMessage + "\n" + msg;
	}

	/** {@inheritDoc} */
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
