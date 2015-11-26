package de.mhus.lib.form;

import de.mhus.lib.core.MProperties;

public class PropertiesDataSource extends DataSource {

	private MProperties properties;

	public MProperties getProperties() {
		return properties;
	}

	public void setProperties(MProperties properties) {
		this.properties = properties;
	}

	@Override
	public boolean getBoolean(UiComponent component, String name, boolean def) {
		return properties.getBoolean(component.getName() + "." + name, def);
	}

	@Override
	public int getInt(UiComponent component, String name, int def) {
		return properties.getInt(component.getName() + "." + name, def);
	}

	@Override
	public String getString(UiComponent component, String name, String def) {
		return properties.getString(component.getName() + "." + name, def);
	}

	@Override
	public Object getObject(UiComponent component, String name, Object def) {
		Object val = properties.getProperty(component.getName() + "." + name);
		if (val == null) return def;
		return val;
	}

	@Override
	public void setObject(UiComponent component, String name, Object value) {
		// TODO Auto-generated method stub
		
	}
	
}
