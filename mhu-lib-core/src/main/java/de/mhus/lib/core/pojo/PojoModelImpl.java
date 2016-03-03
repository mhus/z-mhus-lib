package de.mhus.lib.core.pojo;

import java.util.HashMap;
import java.util.Iterator;

/**
 * <p>PojoModelImpl class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PojoModelImpl implements PojoModel {

	@SuppressWarnings("rawtypes")
	private HashMap<String, PojoAttribute> attributes = new HashMap<String, PojoAttribute>();
	private HashMap<String, PojoAction> actions = new HashMap<String, PojoAction>();
	private Class<?> clazz;
	
	/**
	 * <p>Constructor for PojoModelImpl.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 */
	public PojoModelImpl(Class<?> clazz ) {
		this.clazz = clazz;
	}
	
	/**
	 * <p>addAttribute.</p>
	 *
	 * @param attr a {@link de.mhus.lib.core.pojo.PojoAttribute} object.
	 */
	public void addAttribute(@SuppressWarnings("rawtypes") PojoAttribute attr) {
		attributes.put(attr.getName(),attr);
	}

	/**
	 * <p>addAction.</p>
	 *
	 * @param attr a {@link de.mhus.lib.core.pojo.PojoAction} object.
	 */
	public void addAction(PojoAction attr) {
		actions.put(attr.getName(),attr);
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	@Override
	public Iterator<PojoAttribute> iterator() {
		return attributes.values().iterator();
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	@Override
	public PojoAttribute getAttribute(String name) {
		return attributes.get(name);
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getAttributeNames() {
		return attributes.keySet().toArray(new String[attributes.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getManagedClass() {
		return clazz;
	}

	/**
	 * <p>removeAttribute.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	/**
	 * <p>hasAttribute.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	/**
	 * <p>removeAction.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void removeAction(String name) {
		actions.remove(name);
	}

	/**
	 * <p>hasAction.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean hasAction(String name) {
		return actions.containsKey(name);
	}
	
	/** {@inheritDoc} */
	@Override
	public PojoAction getAction(String name) {
		return actions.get(name);
	}

	/** {@inheritDoc} */
	@Override
	public String[] getActionNames() {
		return actions.keySet().toArray(new String[actions.size()]);
	}

}
