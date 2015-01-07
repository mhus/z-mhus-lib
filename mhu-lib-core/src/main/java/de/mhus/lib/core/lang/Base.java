package de.mhus.lib.core.lang;

import java.util.HashSet;

import de.mhus.lib.core.MSingleton;

public abstract class Base {

	protected Base parent;
	protected HashSet<String> local;

	public Base(Base parent) {
		this.parent = parent;
	}
	
	public abstract void destroy();
	
	public abstract boolean isDestroyed();
	
	public abstract <T> T base(Class<T> ifc);

	public abstract boolean isBase(Class<?> ifc);

	public abstract void addObject(Class<?> ifc, Object obj);
	
	public abstract void removeObject(Class<?> ifc);
	
	public Base getParent() {
		return parent;
	}

	/**
	 * Define this interface as local handled. The base will not ask the parent base to
	 * lookup this interface.
	 * 
	 * @param ifc
	 */
	public void setLocal(Class<?> ifc) {
		if (local == null) local = new HashSet<String>();
		local.add(ifc.getCanonicalName());
	}
	
	public static Base lookup(Object owner) {
		Base base = null;
		if (owner instanceof MObject)
			base = MSingleton.get().getBaseControl().getBaseOf((MObject) owner);
		else
			base = MSingleton.get().getBaseControl().base(owner);
		return base;
	}

	
}
