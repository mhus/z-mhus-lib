package de.mhus.lib.karaf.services;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;

public abstract class AbstractCacheControl extends MLog implements CacheControlIfc {

	protected boolean enabled = true;
	protected boolean supportDisable = true;

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!supportDisable) return;
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return MSystem.toString(this,getSize(),isEnabled() + (supportDisable ? "" : "!") );
	}
	
	@Override
	public String getName() {
		return getClass().getCanonicalName();
	}
	
}
