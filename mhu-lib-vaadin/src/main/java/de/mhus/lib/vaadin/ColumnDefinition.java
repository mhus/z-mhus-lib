package de.mhus.lib.vaadin;

public class ColumnDefinition {

	private String id;
	private Class<?> type;
	private Object def;
	private String title;
	private boolean showByDefault;

	public ColumnDefinition(String id, Class<?> type, Object def, String title, boolean showByDefault) {
		this.id = id;
		this.type = type;
		this.def = def;
		this.title = title;
		this.showByDefault = showByDefault;
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

}
