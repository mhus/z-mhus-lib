package de.mhus.lib.vaadin;

import java.util.Properties;

public class ColumnDefinition {

	private String id;
	private Class<?> type;
	private Object def;
	private String title;
	private boolean showByDefault;
	private Properties properties = null;

	public ColumnDefinition(String id, Class<?> type, Object def, String title, boolean showByDefault) {
		this(id, type, def, title, showByDefault, null);
	}
	
	public ColumnDefinition(String id, Class<?> type, Object def, String title, boolean showByDefault, Properties properties) {
		this.id = id;
		this.type = type;
		this.def = def;
		this.title = title;
		this.showByDefault = showByDefault;
		this.properties = properties;
	}
	
	public String getId() {
		return id;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getDefaultValue() {
		return def;
	}

	public String getTitle() {
		return title;
	}

	public boolean isShowByDefault() {
		return showByDefault;
	}

	public boolean hasPropertiy(String key) {
		return properties != null && properties.containsKey(key);
	}
	
	public String getProperty(String key, String def) {
		if (properties == null) return def;
		return properties.getProperty(key,def);
	}
}
