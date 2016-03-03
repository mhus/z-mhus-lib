package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>ConfigNode class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ConfigNode extends ConfigValue<IConfig>{

	/**
	 * <p>Constructor for ConfigNode.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public ConfigNode(Object owner, String path, IConfig def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected IConfig loadValue() {
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath());
		if (node == null) return getDefault();
		return (IConfig) node;
	}

}
