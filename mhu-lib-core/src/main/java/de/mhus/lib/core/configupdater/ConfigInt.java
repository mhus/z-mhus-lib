package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>ConfigInt class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ConfigInt extends ConfigValue<Integer>{

	/**
	 * <p>Constructor for ConfigInt.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a int.
	 */
	public ConfigInt(Object owner, String path, int def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected Integer loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getConfig(getOwner()).getInt(getPath(), getDefault());
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getInt(getPath().substring(p+1), getDefault());
	}

}
