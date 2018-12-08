/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin;

import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.Table;

import de.mhus.lib.annotations.vaadin.Column;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.vaadin.converter.ObjectConverter;

@SuppressWarnings("deprecation")
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
			MLogUtil.log().d(t);
		}
		return new ObjectConverter();
	}

	public void configureByAnnotation(Column columnDef, boolean canWrite) {
		setEditable(columnDef.editable() && canWrite);
		setConverter(columnDef.converter());
	}
	
	
}
