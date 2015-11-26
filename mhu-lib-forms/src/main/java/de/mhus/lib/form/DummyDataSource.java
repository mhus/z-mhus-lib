package de.mhus.lib.form;

import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

public class DummyDataSource extends DataSource {

	public boolean getBoolean(UiComponent component, String name, boolean def) {
		System.out.println("getBoolean " + component.getName() + "." + name);
		return true;
	}

	public int getInt(UiComponent component, String name, int def) {
		System.out.println("getInt " + component.getName() + "." + name);
		return def;
	}
	
	public String getString(UiComponent component, String name, String def) {
		System.out.println("getString " + component.getName() + "." + name);
		return name;
	}
	
	public Object getObject(UiComponent component, String name, Object def) {
		System.out.println("getObject " + component.getName() + "." + name);
		return def;
	}

	@Override
	public void setObject(UiComponent component, String name, Object value) {
		System.out.println("setObject " + component.getName() + "." + name + ": " + value);
	}

}
