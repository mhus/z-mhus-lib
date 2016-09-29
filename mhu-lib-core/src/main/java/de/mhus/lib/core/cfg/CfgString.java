package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>CfgString class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class CfgString extends CfgValue<String>{

	/**
	 * <p>Constructor for CfgString.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a {@link java.lang.String} object.
	 */
	public CfgString(Object owner, String path, String def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected String loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MSingleton.getCfg(getOwner()).getString(getPath(), getDefault());
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getString(getPath().substring(p+1), getDefault());
	}

}
