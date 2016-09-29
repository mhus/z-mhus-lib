package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>PropertiesSubset class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PropertiesSubset extends AbstractProperties {

	private IProperties parent;
	private String prefix;
	private boolean readonly;
	
	/**
	 * <p>Constructor for PropertiesSubset.</p>
	 *
	 * @param parent a {@link de.mhus.lib.core.IProperties} object.
	 * @param prefix a {@link java.lang.String} object.
	 */
	public PropertiesSubset(IProperties parent, String prefix) {
		this(parent,prefix,false);
	}
	
	/**
	 * <p>Constructor for PropertiesSubset.</p>
	 *
	 * @param parent a {@link de.mhus.lib.core.IProperties} object.
	 * @param prefix a {@link java.lang.String} object.
	 * @param readonly a boolean.
	 */
	public PropertiesSubset(IProperties parent, String prefix, boolean readonly) {
		this.parent = parent;
		this.prefix = prefix;
		this.readonly = readonly;
	}
	
	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return parent.get(prefix + name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return parent.isProperty(prefix + name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
		if (readonly)
			throw new NotSupportedException();
		parent.removeProperty(prefix + key);
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
		if (readonly)
			throw new NotSupportedException();
		parent.put(prefix + key, value);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		if (readonly) return false;
		return parent.isEditable();
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		HashSet<String> out = new HashSet<>();
		for (String k : parent.keys())
			if (k.startsWith(prefix))
				out.add(k);
		return out;
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		int cnt = 0;
		for (String k : parent.keys())
			if (k.startsWith(prefix))
				cnt++;
		return cnt;
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(Object value) {
		throw new NotSupportedException(); //TODO implement
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Object> values() {
		throw new NotSupportedException(); //TODO implement
	}

	/** {@inheritDoc} */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new NotSupportedException(); //TODO implement
	}

}
