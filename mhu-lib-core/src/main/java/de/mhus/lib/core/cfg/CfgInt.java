package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>CfgInt class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class CfgInt extends CfgValue<Integer>{

	/**
	 * <p>Constructor for CfgInt.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a int.
	 */
	public CfgInt(Object owner, String path, int def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected Integer loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getCfg(getOwner()).getInt(getPath(), getDefault());
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getInt(getPath().substring(p+1), getDefault());
	}

}
