package de.mhus.lib.vaadin;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Table;

import de.mhus.lib.annotations.vaadin.Column;

public class ColumnModel {

	private Table table;
	private String colId;
	private boolean editable = true;
	private Class<?> converter;

	public ColumnModel(Table table, String colId) {
		this.table = table;
		this.colId = colId;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getPropertyId() {
		return colId;
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setCollapsed(boolean collapsed) {
		table.setColumnCollapsed(colId, collapsed);
	}

	public Class<?> getConverter() {
		return converter;
	}

	public void setConverter(Class<?> converter) {
		if (converter == Object.class)
			this.converter = null;
		else
			this.converter = converter;
	}

	@SuppressWarnings("unchecked")
	public Converter<String, ?> generateConverter(Class<?> type) {
		try {
			if (converter != null) return (Converter<String, ?>) converter.newInstance();
			converter = MhuTable.findDefaultConverter(this, type);
			if (converter != null) return (Converter<String, ?>) converter.newInstance();
		} catch (Throwable t) {
			//TODO log output
		}
		return null;
	}

	public void configureByAnnotation(Column columnDef, boolean canWrite) {
		setEditable(columnDef.editable() && canWrite);
		setConverter(columnDef.converter());
	}
	
	
}
