package de.mhus.lib.form;

public abstract class DataSource {

	public static final String ENABLED = "enabled";
	public static final String VISIBLE = "visible";
	public static final String VALUE = "";
	public static final String CAPTION = "caption";
	public static final String EDITOR_EDITABLE = "editable";
	
	public abstract boolean getBoolean(UiComponent component, String name, boolean def);

	public abstract int getInt(UiComponent component, String name, int def);

	public abstract String getString(UiComponent component, String name, String def);
	
	public abstract Object getObject(UiComponent component, String name, Object def);

	public abstract void setObject(UiComponent component, String name, Object value);

	
}
