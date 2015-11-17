package de.mhus.lib.form;

public class DataSource {

	public static final String ENABLED = "enabled";
	public static final String VISIBLE = "visible";
	public static final String VALUE = "";
	public static final String CAPTION = "caption";
	public static final String EDITOR_EDITABLE = "editor_editable";
	
	public boolean getBoolean(UiComponent component, String name, boolean def) {
		return def;
	}

	public int getInt(UiComponent component, String name, int def) {
		return def;
	}
	
	public String getString(UiComponent component, String name, String def) {
		return def;
	}
	
	public Object getObject(UiComponent component, String name, Object def) {
		return def;
	}
	
}
