package de.mhus.lib.core.lang;

import java.util.HashSet;

import de.mhus.lib.core.MSingleton;

/**
 * <p>Abstract Base class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class Base {

	protected Base parent;
	protected HashSet<String> local;

	/**
	 * <p>Constructor for Base.</p>
	 *
	 * @param parent a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public Base(Base parent) {
		this.parent = parent;
	}
	
	/**
	 * <p>destroy.</p>
	 */
	public abstract void destroy();
	
	/**
	 * <p>isDestroyed.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isDestroyed();
	
	/**
	 * <p>lookup.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public abstract <T> T lookup(Class<T> ifc);

	/**
	 * <p>isBase.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	public abstract boolean isBase(Class<?> ifc);

	/**
	 * <p>addObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param obj a {@link java.lang.Object} object.
	 */
	public abstract void addObject(Class<?> ifc, Object obj);
	
	/**
	 * <p>removeObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 */
	public abstract void removeObject(Class<?> ifc);
	
	/**
	 * <p>Getter for the field <code>parent</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public Base getParent() {
		return parent;
	}

	/**
	 * Define this interface as local handled. The base will not ask the parent base to
	 * lookup this interface.
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 */
	public void setLocal(Class<?> ifc) {
		if (local == null) local = new HashSet<String>();
		local.add(ifc.getCanonicalName());
	}
	
	
}
