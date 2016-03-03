package de.mhus.lib.form.binding;

import java.util.HashMap;
import java.util.Set;

import de.mhus.lib.core.MString;
import de.mhus.lib.form.DataSource;

/**
 * <p>PackagingDataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PackagingDataSource extends DataSource {

	private HashMap<String, DataSource> packages = new HashMap<String, DataSource>();
	private DataSource defaultSource;
	
	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		return pack.getProperty(name);
	}

	/**
	 * <p>fixName.</p>
	 *
	 * @param pack a {@link de.mhus.lib.form.DataSource} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String fixName(DataSource pack, String name) {
		if (pack != defaultSource) name = MString.afterIndex(name, '.');
		return name;
	}

	/**
	 * <p>getPack.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.form.DataSource} object.
	 */
	protected DataSource getPack(String name) {
		if (name == null) return defaultSource;
		String packName = MString.beforeIndex(name, '.');
		if (packName.length() == 0) return defaultSource;
		DataSource pack = packages.get(packName);
		return pack == null ? defaultSource : pack;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		return pack.isProperty(name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		pack.removeProperty(name);
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String name, Object value) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		pack.setProperty(name, value);
		if (isConnected()) fireValueChanged(name);
	}

	/** {@inheritDoc} */
	@Override
	public void setPropertyData(String name, Object value) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		pack.setProperty(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return null;
	}

	/**
	 * <p>Getter for the field <code>defaultSource</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.DataSource} object.
	 */
	public DataSource getDefaultSource() {
		return defaultSource;
	}

	/**
	 * <p>Setter for the field <code>defaultSource</code>.</p>
	 *
	 * @param defaultSource a {@link de.mhus.lib.form.DataSource} object.
	 */
	public void setDefaultSource(DataSource defaultSource) {
		this.defaultSource = defaultSource;
	}

	/**
	 * <p>addPackage.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param handler a {@link de.mhus.lib.form.DataSource} object.
	 */
	public void addPackage(String name, DataSource handler) {
		packages.put(name, handler);
	}
	
	/**
	 * <p>removePackage.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void removePackage(String name) {
		packages.remove(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isPropertyPossible(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		return pack.isPropertyPossible(name);
	}
	
}
