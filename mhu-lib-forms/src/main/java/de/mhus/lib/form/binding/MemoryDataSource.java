package de.mhus.lib.form.binding;

import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.util.SetCast;
import de.mhus.lib.form.DataSource;

/**
 * <p>MemoryDataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MemoryDataSource extends DataSource {

	protected Properties properties = null;


	/**
	 * <p>Constructor for MemoryDataSource.</p>
	 */
	public MemoryDataSource() {
		this(new Properties());
	}
	
	/**
	 * <p>Constructor for MemoryDataSource.</p>
	 *
	 * @param properties a {@link java.util.Properties} object.
	 */
	public MemoryDataSource(Properties properties) {
		this.properties = properties;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return properties.getProperty(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isPropertyPossible(String name) {
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}

	/** {@inheritDoc} */
	@Override
	public void setPropertyData(String key, Object value) {
		properties.setProperty(key, MCast.objectToString(value) );
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return new SetCast<Object, String>(properties.keySet());
	}
	
	/**
	 * <p>getMemory.</p>
	 *
	 * @return a {@link java.util.Properties} object.
	 */
	public Properties getMemory() {
		return properties;
	}

}
