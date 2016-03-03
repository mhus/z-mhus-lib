package de.mhus.lib.core.lang;

import de.mhus.lib.annotations.pojo.Hidden;
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
	
	@Hidden
	private Base base;
	@Hidden
	private Base originalBase;
	@Hidden
	private Base oldBase;

	/**
	 * <p>Constructor for MObject.</p>
	 */
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
	
	/**
	 * <p>base.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	protected <T> T base(Class<T> ifc) {
		try {
//			initBase();
			return base.lookup(ifc);
		} catch (Exception e) {
			if (MSingleton.isDirtyTrace())
				e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * <p>isBase.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
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
	
	/**
	 * <p>forkBase.</p>
	 */
	protected void forkBase() {
		synchronized (this) {
			if (originalBase != null) return; // not possible
//			initBase();
			base = MSingleton.get().getBaseControl().createBase(this, base);
			
		}
	}
	
	/**
	 * <p>createBase.</p>
	 */
	protected void createBase() {
		synchronized (this) {
			if (originalBase != null) return;
			originalBase = base;
			base = MSingleton.get().getBaseControl().createBase(this, base);
			MSingleton.get().getBaseControl().installBase(base);
		}
	}

	/**
	 * <p>installBase.</p>
	 */
	protected void installBase() {
		synchronized (this) {
			oldBase = MSingleton.get().getBaseControl().installBase(base);
		}
	}
	
	/**
	 * <p>leaveBase.</p>
	 */
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
	
	/**
	 * <p>base.</p>
	 *
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 */
	protected Base base() {
		return base;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MSystem.toString(this);
	}

}
