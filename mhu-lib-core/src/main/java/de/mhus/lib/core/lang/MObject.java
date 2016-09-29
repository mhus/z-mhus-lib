package de.mhus.lib.core.lang;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

/**
 * <p>MObject class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MObject extends MLog {
	
	/**
	 * <p>Constructor for MObject.</p>
	 */
	public MObject() {
		BaseControl control = MSingleton.get().getBaseControl();
		control.inject(this, MSingleton.get().getBaseControl().base(this) );
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MSystem.toString(this);
	}

}
