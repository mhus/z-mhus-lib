package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>ConfigString class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ConfigString extends ConfigValue<String>{

	/**
	 * <p>Constructor for ConfigString.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a {@link java.lang.String} object.
	 */
	public ConfigString(Object owner, String path, String def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected String loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getConfig(getOwner()).getString(getPath(), getDefault());
		ResourceNode node = MSingleton.getConfig(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getString(getPath().substring(p+1), getDefault());
	}

}
