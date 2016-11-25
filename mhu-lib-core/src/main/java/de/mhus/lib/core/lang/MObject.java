package de.mhus.lib.core.lang;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.core.util.Nls;

public class MObject extends MLog implements MNlsProvider, Nls {
	
	private MNls nls;

	public MObject() {
	}

	@Override
	public String toString() {
		return MSystem.toString(this);
	}

	@Override
	public String nls(String text) {
		return getNls().find(text);
	}

	@Override
	public MNls getNls() {
		if (nls == null)
			nls = MNls.lookup(this);
		return nls;
	}

}
