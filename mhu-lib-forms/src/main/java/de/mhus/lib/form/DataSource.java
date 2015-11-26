package de.mhus.lib.form;

public interface DataSource {

	public static final String ENABLED = "enabled";
	public static final String VISIBLE = "visible";
	public static final String VALUE = "";
	public static final String CAPTION = "caption";
	public static final String EDITOR_EDITABLE = "editable";
	
	boolean getBoolean(UiComponent component, String name, boolean def);

	int getInt(UiComponent component, String name, int def);

	String getString(UiComponent component, String name, String def);
	
	Object getObject(UiComponent component, String name, Object def);

	void setObject(UiComponent component, String name, Object value);

	
}
