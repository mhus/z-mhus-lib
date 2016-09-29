package de.mhus.lib.logging.level;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;

/**
 * <p>GeneralMapper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class GeneralMapper implements LevelMapper {

	private ThreadMapperConfig config;
	
	/** {@inheritDoc} */
	@Override
	public LEVEL map(Log log, LEVEL level, Object... msg) {
		if (config == null) return level;
		return config.map(log, level, msg);
	}

	/** {@inheritDoc} */
	@Override
	public void prepareMessage(Log log, StringBuffer msg) {
		msg.append('(').append(Thread.currentThread().getId()).append(')');
	}

	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.logging.level.ThreadMapperConfig} object.
	 */
	public ThreadMapperConfig getConfig() {
		return config;
	}

	/**
	 * <p>Setter for the field <code>config</code>.</p>
	 *
	 * @param config a {@link de.mhus.lib.logging.level.ThreadMapperConfig} object.
	 */
	public void setConfig(ThreadMapperConfig config) {
		this.config = config;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MSystem.toString(this, config);
	}
	
}
