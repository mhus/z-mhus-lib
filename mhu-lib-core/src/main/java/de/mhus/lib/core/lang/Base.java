package de.mhus.lib.core.lang;

import java.util.HashSet;

public abstract class Base {

	protected Base parent;
	protected HashSet<String> local;

	public Base(Base parent) {
		this.parent = parent;
	}
	
	public abstract void destroy();
	
	public abstract boolean isDestroyed();
	
	public <T> T lookup(Class<T> ifc) {
		return lookup(ifc, null);
	}

	public abstract <T, D extends T> T lookup(Class<T> ifc, Class<D> def);
	
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
	
	
}
