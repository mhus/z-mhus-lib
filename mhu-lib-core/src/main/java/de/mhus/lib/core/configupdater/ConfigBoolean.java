package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>ConfigBoolean class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ConfigBoolean extends ConfigValue<Boolean>{

	/**
	 * <p>Constructor for ConfigBoolean.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a boolean.
	 */
	public ConfigBoolean(Object owner, String path, boolean def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected Boolean loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getConfig(getOwner()).getBoolean(getPath(), getDefault());
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getBoolean(getPath().substring(p+1), getDefault());
	}

}
