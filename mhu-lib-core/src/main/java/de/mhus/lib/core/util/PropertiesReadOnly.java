package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Set;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>PropertiesReadOnly class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PropertiesReadOnly extends AbstractProperties {

	private IProperties parent;

	/**
	 * <p>Constructor for PropertiesReadOnly.</p>
	 *
	 * @param parent a {@link de.mhus.lib.core.IProperties} object.
	 */
	public PropertiesReadOnly(IProperties parent) {
		this.parent = parent;
	}
	
	/** {@inheritDoc} */
	@Override
	public Object get(Object name) {
		return parent.get(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return parent.isProperty(name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return parent.keys();
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return parent.size();
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return get(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(Object value) {
		return parent.containsValue(value);
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Object> values() {
		return parent.values();
	}

	/** {@inheritDoc} */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return parent.entrySet();
	}

}
