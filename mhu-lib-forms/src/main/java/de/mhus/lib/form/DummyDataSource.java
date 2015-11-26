package de.mhus.lib.form;

import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.UiComponent;

public class DummyDataSource extends DataSource {

	public boolean getBoolean(UiComponent component, String name, boolean def) {
		System.out.println("getBoolean " + component.getName() + "." + name);
		return true;
	}

	public int getInt(UiComponent component, String name, int def) {
		System.out.println("getInt " + name);
		return def;
	}
	
	public String getString(UiComponent component, String name, String def) {
		System.out.println("getString " + name);
		return name;
	}
	
	public Object getObject(UiComponent component, String name, Object def) {
		System.out.println("getObject " + name);
		return def;
	}

}
