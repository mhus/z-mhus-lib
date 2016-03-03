package de.mhus.lib.core.util;

import java.util.Set;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>PropertiesReadOnly class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class PropertiesReadOnly extends IProperties {

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
	public Object getProperty(String name) {
		return parent.getProperty(name);
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

}
