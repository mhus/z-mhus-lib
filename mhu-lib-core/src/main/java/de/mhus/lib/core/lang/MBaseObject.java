package de.mhus.lib.core.lang;

import de.mhus.lib.core.MSingleton;

public class MBaseObject extends MObject {

	public MBaseObject() {
		BaseControl control = MSingleton.get().getBaseControl();
		control.inject(this, MSingleton.get().getBaseControl().base() );
	}

}
