package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>CfgLong class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class CfgLong extends CfgValue<Long>{

	/**
	 * <p>Constructor for CfgLong.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a long.
	 */
	public CfgLong(Object owner, String path, long def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected Long loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getCfg(getOwner()).getLong(getPath(), getDefault());
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getLong(getPath().substring(p+1), getDefault());
	}

}
