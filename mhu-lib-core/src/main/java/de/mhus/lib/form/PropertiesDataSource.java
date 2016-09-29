package de.mhus.lib.form;

import de.mhus.lib.core.MProperties;

/**
 * <p>PropertiesDataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class PropertiesDataSource implements DataSource {

	private MProperties properties;

	/**
	 * <p>Getter for the field <code>properties</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MProperties} object.
	 */
	public MProperties getProperties() {
		return properties;
	}

	/**
	 * <p>Setter for the field <code>properties</code>.</p>
	 *
	 * @param properties a {@link de.mhus.lib.core.MProperties} object.
	 */
	public void setProperties(MProperties properties) {
		this.properties = properties;
	}

	/** {@inheritDoc} */
	@Override
	public boolean getBoolean(UiComponent component, String name, boolean def) {
		return properties.getBoolean(component.getName() + "." + name, def);
	}

	/** {@inheritDoc} */
	@Override
	public int getInt(UiComponent component, String name, int def) {
		return properties.getInt(component.getName() + "." + name, def);
	}

	/** {@inheritDoc} */
	@Override
	public String getString(UiComponent component, String name, String def) {
		return properties.getString(component.getName() + "." + name, def);
	}

	/** {@inheritDoc} */
	@Override
	public Object getObject(UiComponent component, String name, Object def) {
		Object val = properties.getProperty(component.getName() + "." + name);
		if (val == null) return def;
		return val;
	}

	/** {@inheritDoc} */
	@Override
	public void setObject(UiComponent component, String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	/** {@inheritDoc} */
	@Override
	public DataSource getNext() {
		return null;
	}
	
}
