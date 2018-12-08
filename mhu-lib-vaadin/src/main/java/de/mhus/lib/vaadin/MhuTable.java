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

import java.sql.Time;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Component;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.data.util.converter.StringToBooleanConverter;
import com.vaadin.v7.data.util.converter.StringToDoubleConverter;
import com.vaadin.v7.data.util.converter.StringToFloatConverter;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.v7.data.util.converter.StringToLongConverter;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TableFieldFactory;
import com.vaadin.v7.ui.TextField;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.lang.DateTime;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.vaadin.converter.BooleanPrimitiveConverter;
import de.mhus.lib.vaadin.converter.DateConverter;
import de.mhus.lib.vaadin.converter.DateTimeConverter;
import de.mhus.lib.vaadin.converter.DoublePrimitiveConverter;
import de.mhus.lib.vaadin.converter.FloatPrimitiveConverter;
import de.mhus.lib.vaadin.converter.IntPrimitiveConverter;
import de.mhus.lib.vaadin.converter.LongPrimitiveConverter;
import de.mhus.lib.vaadin.converter.SqlDateConverter;
import de.mhus.lib.vaadin.converter.SqlTimeConverter;

@SuppressWarnings({"serial","deprecation"})
public class MhuTable extends ExpandingTable {
	
	private static final long serialVersionUID = 1L;
//	protected static final Action ACTION_ENTER = new ShortcutAction("Enter", ShortcutAction.KeyCode.ENTER,null);
//	protected static final Action ACTION_ESCAPE = new ShortcutAction("Escape",ShortcutAction.KeyCode.ESCAPE,null);
	
	private static Log log = Log.getLog(MhuTable.class);
	protected Object editableId;
	private boolean tableEditable = false;
	private HashMap<String, ColumnModel> columnModels = new HashMap<>();
	
	public MhuTable() {
		super();
	}

	public MhuTable(String caption, Container dataSource) {
		super(caption, dataSource);
	}

	public MhuTable(String caption) {
		super(caption);
	}


	@Override
	protected void initUI() {

		setTableFieldFactory(new TableFieldFactory() {
			
			@Override
			public Field<?> createField(Container container,
					Object itemId, Object propertyId, Component uiContext) {
		        Field<?> f = MhuTable.this.createField(container,itemId,propertyId,uiContext);
		        return f;
			}
		});
		
		addItemClickListener(new ItemClickListener() {
         @Override
         public void itemClick(ItemClickEvent event) {
        	 if (tableEditable && event.getButton() == MouseButton.LEFT && event.isDoubleClick()) {
        		 doActionEditable(event.getItemId());
	         } else if (event.getButton() == MouseButton.LEFT) {
		         doActionEditable(null);
	         }
         	}
         });
			
		super.initUI();
	}
	
	@SuppressWarnings("unchecked")
	protected Field<?> createField(Container container,
			Object itemId, Object propertyId, Component uiContext) {
		
		ColumnModel model = getColumnModel(String.valueOf(propertyId));
		boolean editable = tableEditable && itemId.equals(editableId) && model.isEditable() ;
        Field<?> f = null;
        Class<?> type = container.getType(propertyId);
        f = createFieldForType(type);
        f.setCaption(null);
        f.setWidth("100%");
        f.setReadOnly(!editable);
        if (f instanceof AbstractField) {
        	if (model.getConverter() == null) {
        		model.setConverter(findDefaultConverter(model,type));
        	}
        	if (model.getConverter() != null)
        		((AbstractField<String>)f).setConverter((Converter<String, ?>)model.generateConverter(type));
        }
        return f;
	}


	protected boolean doActionSave() {
		return false;
	}


	public void doActionEditable(Object itemId) {
        editableId = itemId;
        if (itemId == null) {
	         setEditable(false);
	         removeStyleName("editable");
        } else {
	        addStyleName("editable");
	        setEditable(true);
        }
	}
	
	public Object getEditedObject() {
		return editableId;
	}

	public static Class<?> findDefaultConverter(ColumnModel model, Class<?> type) {
		log.t("Default Converter",model.getPropertyId(),type);
		
		if (type == int.class)
			return IntPrimitiveConverter.class;
		if (type == boolean.class)
			return BooleanPrimitiveConverter.class;
		if (type == long.class)
			return LongPrimitiveConverter.class;
		if (type == double.class)
			return DoublePrimitiveConverter.class;
		if (type == float.class)
			return FloatPrimitiveConverter.class;
		
		if (type == Integer.class)
			return StringToIntegerConverter.class;
		if (type == Time.class)
			return SqlTimeConverter.class;
		if (type == DateTime.class)
			return DateTimeConverter.class;
		if (type == java.sql.Date.class)
			return SqlDateConverter.class;
		if (type == java.util.Date.class)
			return DateConverter.class;
		if (type == Boolean.class)
			return StringToBooleanConverter.class;
		if (type == Long.class)
			return StringToLongConverter.class;
		if (type == Double.class)
			return StringToDoubleConverter.class;
		if (type == Float.class)
			return StringToFloatConverter.class;
		
		return null;
	}

	protected Field<?> createFieldForType(Class<?> type) {
//        if (type == Boolean.class || type == boolean.class) {
//        	return new CheckBox();
//        } else {
        	return new TextField();
//        }
	}

	public boolean isTableEditable() {
		return tableEditable;
	}

	public void setTableEditable(boolean tableEditable) {
		this.tableEditable = tableEditable;
	}
	
	public Collection<?> getSelectedValues() {
		Object o = getValue();
		if (o == null) return new LinkedList<Object>();
		// this should not happen ...
		if (o instanceof Collection) return (Collection<?>)o;
		LinkedList<Object> out = new LinkedList<Object>();
		if (o instanceof Object[]) {
			MCollection.copyArray((Object[])o, out);
		} else
			out.add(o);
		return out;
	}

	public ColumnModel getColumnModel(String colId) {
		synchronized (columnModels) {
			ColumnModel ret = columnModels.get(colId);
			if (ret == null) {
				ret = new ColumnModel(this, colId);
				columnModels.put(colId, ret);
			}
			return ret;
		}
	}
	
}
