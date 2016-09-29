package de.mhus.lib.core.cfg;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.util.PropertiesSubset;

/**
 * <p>CfgProperties class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class CfgProperties extends CfgValue<IProperties> {

	/**
	 * <p>Constructor for CfgProperties.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 */
	public CfgProperties(Object owner, String path) {
		super(owner, path, new MProperties() );
	}

	/** {@inheritDoc} */
	@Override
	protected IProperties loadValue() {

		ResourceNode node = MSingleton.getCfg(getOwner(), null);
		if (node == null) return getDefault();
		if (MString.isEmpty(getPath()))
			return node;
		
		return new PropertiesSubset(node, getPath());
	}

}
