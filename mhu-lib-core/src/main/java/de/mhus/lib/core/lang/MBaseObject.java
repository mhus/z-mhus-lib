package de.mhus.lib.core.lang;

import de.mhus.lib.core.MApi;

public class MBaseObject extends MObject {

	public MBaseObject() {
		BaseControl control = MApi.get().getBaseControl();
		control.inject(this, MApi.get().getBaseControl().base() );
	}

}
