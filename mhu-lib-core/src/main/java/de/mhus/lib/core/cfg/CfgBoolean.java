package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>CfgBoolean class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class CfgBoolean extends CfgValue<Boolean>{

	/**
	 * <p>Constructor for CfgBoolean.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a boolean.
	 */
	public CfgBoolean(Object owner, String path, boolean def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected Boolean loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getCfg(getOwner()).getBoolean(getPath(), getDefault());
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getBoolean(getPath().substring(p+1), getDefault());
	}

}
