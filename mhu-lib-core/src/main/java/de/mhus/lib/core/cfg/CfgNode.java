package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>CfgNode class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class CfgNode extends CfgValue<IConfig>{

	/**
	 * <p>Constructor for CfgNode.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a {@link de.mhus.lib.core.config.IConfig} object.
	 */
	public CfgNode(Object owner, String path, IConfig def) {
		super(owner, path, def);
	}

	/** {@inheritDoc} */
	@Override
	protected IConfig loadValue() {
		ResourceNode node = MSingleton.getCfg(getOwner()).getNodeByPath(getPath());
		if (node == null) return getDefault();
		return (IConfig) node;
	}

}
