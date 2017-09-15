package de.mhus.lib.mutable;

import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;

public class KarafBaseControl extends BaseControl {

	Base defaultBase = new KarafBase(null);
	
	@Override
	public Base base() {
		return defaultBase;
	}
}
