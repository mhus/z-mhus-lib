package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>ConfigLong class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ConfigLong extends ConfigValue<Long>{

	/**
	 * <p>Constructor for ConfigLong.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a long.
	 */
	public ConfigLong(Object owner, String path, long def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected Long loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getConfig(getOwner()).getLong(getPath(), getDefault());
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getLong(getPath().substring(p+1), getDefault());
	}

}
