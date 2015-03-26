package de.mhus.lib.core.lang;

import de.mhus.lib.annotations.pojo.Hidden;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

public class MObject extends MLog {
	
	@Hidden
	private Base base;
	@Hidden
	private Base originalBase;
	@Hidden
	private Base oldBase;

	public MObject() {
		initBase();
	}
			
	private synchronized MObject initBase() {
		if (base == null || base.isDestroyed()) {
			BaseControl control = MSingleton.get().getBaseControl();
			base = control.base(this);
			control.inject(this, base);
		}
		return this;
	}
	
	protected <T> T base(Class<T> ifc) {
		try {
//			initBase();
			return base.base(ifc);
		} catch (Exception e) {
			if (MSingleton.isDirtyTrace())
				e.printStackTrace();
			return null;
		}
	}
	
	protected boolean isBase(Class<?> ifc) {
		try {
//			initBase();
			return base.isBase(ifc);
		} catch (Exception e) {
			if (MSingleton.isDirtyTrace())
				e.printStackTrace();
			return false;
		}
	}
	
	protected void forkBase() {
		synchronized (this) {
			if (originalBase != null) return; // not possible
//			initBase();
			base = MSingleton.get().getBaseControl().createBase(this, base);
			
		}
	}
	
	protected void createBase() {
		synchronized (this) {
			if (originalBase != null) return;
			originalBase = base;
			base = MSingleton.get().getBaseControl().createBase(this, base);
			MSingleton.get().getBaseControl().installBase(base);
		}
	}

	protected void installBase() {
		synchronized (this) {
			oldBase = MSingleton.get().getBaseControl().installBase(base);
		}
	}
	
	protected void leaveBase() {
		synchronized (this) {
			if (oldBase != null) {
				MSingleton.get().getBaseControl().installBase(oldBase);
				oldBase = null;
				return;
			}
			if (originalBase == null) return;
			base = originalBase;
			MSingleton.get().getBaseControl().installBase(base);
			originalBase = null;
		}
	}
	
	protected Base base() {
		return base;
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this);
	}

}
