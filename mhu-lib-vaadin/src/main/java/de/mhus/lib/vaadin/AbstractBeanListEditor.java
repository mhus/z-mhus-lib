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

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;

import de.mhus.lib.annotations.vaadin.Column;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.core.util.FilterRequest;
import de.mhus.lib.core.util.MNls;

public abstract class AbstractBeanListEditor<E> extends AbstractListEditor<E> {

	private static final long serialVersionUID = 1L;
	protected Class<E> beanClass;
	protected PojoModel beanModel;
	protected LinkedList<ColumnDefinition> columnDefinitions;
	private static Log log = Log.getLog(AbstractBeanListEditor.class);

	public AbstractBeanListEditor(Class<E> beanClass, String schema) {
		this.beanClass = beanClass;
		beanModel = new PojoParser().parse(beanClass).getModel();

		TreeMap<Integer,String> columns = new TreeMap<Integer, String>();
		int nextOrderId = 100;
		for (PojoAttribute<?> descriptor : beanModel) {
			
			Column columnDef = descriptor.getAnnotation(Column.class);
			if (columnDef != null) {
				
				if (
						MString.isEmpty(schema) && columnDef.schema().length == 0
						||
						MCollection.contains(columnDef.schema(), schema)
						) {
					int order = columnDef.order();
					if (order < 0)
						order = ++nextOrderId;
					columns.put(order, descriptor.getName());
				}
				
			}
		}
		
		columnDefinitions = new LinkedList<>();
		for (String colId : columns.values()) {
			PojoAttribute<?> descriptor = beanModel.getAttribute(colId);
			Column columnDef = descriptor.getAnnotation(Column.class);
			ColumnDefinition def = new ColumnDefinition(colId, descriptor.getType(), createDefaultvalue(descriptor), MNls.find(this, columnDef.nls() + "=" + columnDef.title()),columnDef.elapsed(), createProperties(columnDef));
			columnDefinitions.add(def);
		}
		

	}
	
	private Properties createProperties(Column columnDef) {
		if (columnDef.properties() == null || columnDef.properties().length == 0) return null;
		return MProperties.explodeToProperties( columnDef.properties() );
	}

	@Override
	public void initUI() {
		super.initUI();
		for (ColumnDefinition def : columnDefinitions)
			filter.addKnownFacetName(def.getId() + ":");
	}
	
	protected Object createDefaultvalue(PojoAttribute<?> descriptor) {
		if (descriptor.getType() == String.class) return "";
		if (descriptor.getType() == int.class) return 0;
		if (descriptor.getType() == Integer.class) return 0;
		if (descriptor.getType() == long.class) return 0l;
		if (descriptor.getType() == Long.class) return 0l;
		if (descriptor.getType() == double.class) return 0d;
		if (descriptor.getType() == Double.class) return 0d;
		if (descriptor.getType() == float.class) return 0f;
		if (descriptor.getType() == Float.class) return 0f;
		if (descriptor.getType() == boolean.class) return false;
		if (descriptor.getType() == Boolean.class) return false;
		if (descriptor.getType() == char.class) return ' ';
		if (descriptor.getType() == Character.class) return ' ';
		if (descriptor.getType() == byte.class) return (byte)0;
		if (descriptor.getType() == Byte.class) return (byte)0;
		if (descriptor.getType() == short.class) return (short)0;
		if (descriptor.getType() == Short.class) return (short)0;
		if (descriptor.getType() == Date.class) return new Date();
		if (descriptor.getType() == java.sql.Date.class) return new java.sql.Date(System.currentTimeMillis());
		if (descriptor.getType() == UUID.class) return UUID.randomUUID();
		return null;
	}

	@Override
	protected ColumnDefinition[] createColumnDefinitions() {
		return columnDefinitions.toArray(new ColumnDefinition[columnDefinitions.size()]);
	}

	@Override
	protected Object[] getValues(E entry) {
		Object[] out = new Object[ columnDefinitions.size()];
		int cnt = 0;
		for (ColumnDefinition def : columnDefinitions) {
			PojoAttribute<?> attribute = beanModel.getAttribute(def.getId());
			try {
				Object value = attribute.get(entry);
				out[cnt] = value;
			} catch (Throwable t) {
//				t.printStackTrace(); //TODO log?
				out[cnt] = def.getDefaultValue();
			}
			cnt++;
		}
		return out;
	}
	
	@Override
	protected List<E> createDataList(FilterRequest filter) {
		return filterDataList(createBeanDataList(), filter);
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract List<E> createBeanDataList();

	protected List<E> filterDataList(List<E> list, FilterRequest filter) {
		if (filter == null || !filter.isFiltering())
			return list;
		LinkedList<E> out = new LinkedList<>();
		for (E item : list) {
			if (isPassFilter(item, filter))
				out.add(item);
		}
		return out;
	}

	protected boolean isPassFilter(E item, FilterRequest filter) {
		
		boolean ok = false;
		boolean done = false;
		
		for (String mask : filter.getText()) {
			if (MString.isSet(mask)) {
				done = true;
				for (PojoAttribute<?> attr : beanModel) {
					try {
						String value = valueOf(attr.get(item));
						if (value != null && MString.compareFsLikePattern(value.toLowerCase(),mask.toLowerCase())) {
							log.d("filter match general", item, mask, value);
							ok = true;
							break;
						}
					} catch (IOException e) {
					}
				}
			}
		}
		if (!done) ok = true;
		if (!ok) return false;
		
		done = false;
		ok = false;
		
		for (PojoAttribute<?> attr : beanModel) {
			try {
				String facet = filter.getFacet(attr.getName());
				if (facet != null) {
					done = true;
					String value = valueOf(attr.get(item));
					if (value != null && MString.compareFsLikePattern(value.toLowerCase(),facet.toLowerCase())) {
						log.d("filter match facet", item, attr.getName(), facet, value);
						return true;
					}
				}
			} catch (IOException e) {
			}
		}
		if (!done) ok = true;
	
		return ok;
	}

	private String valueOf(Object in) {
		if (in == null) return null;
		return String.valueOf(in);
	}

}
