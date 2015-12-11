package de.mhus.lib.core.lang;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

public class MObject extends MLog {
	
	public MObject() {
		BaseControl control = MSingleton.get().getBaseControl();
		control.inject(this, MSingleton.get().getBaseControl().base(this) );
	}

	@Override
	public String toString() {
		return MSystem.toString(this);
	}

}
