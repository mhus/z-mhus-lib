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

import java.util.LinkedList;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;

import de.mhus.lib.core.logging.MLogUtil;

@SuppressWarnings("deprecation")
public class SimpleTable extends ExpandingTable {

	private static final long serialVersionUID = 1L;
	private IndexedContainer dataSource;
	private ColumnDefinition[] columns;
	//protected MProperties status = new MProperties();

	public SimpleTable() {
	}

	public SimpleTable(String caption, Container dataSource) {
		super(caption, dataSource);
	}

	public SimpleTable(String caption) {
		super(caption);
	}

	@Override
	protected void initUI() {
        setColumnReorderingAllowed(true);
        setColumnCollapsingAllowed(true);
        setSizeFull();
        super.initUI();
	}

	public void createDataSource(ColumnDefinition ... columns) {
		this.columns = columns;
        dataSource = new IndexedContainer();
        LinkedList<Object> columnList = new LinkedList<>();
        LinkedList<Object> colapsedByDefault = new LinkedList<>();
        for (ColumnDefinition column : columns) {
        	dataSource.addContainerProperty(column.getId(), column.getType(), column.getDefaultValue());
        	setColumnHeader(column.getId(), column.getTitle());
        	if (!column.isShowByDefault())
        		colapsedByDefault.add(column.getId());
        	columnList.add(column.getId());
        }
        
        setContainerDataSource(dataSource);

        setVisibleColumns(columnList.toArray(new Object[colapsedByDefault.size()]));
        
        for (Object col : colapsedByDefault)
        	setColumnCollapsed(col, true);
                
	}

	public IndexedContainer getDataSource() {
		return dataSource;
	}

	public ColumnDefinition[] getColumns() {
		return columns;
	}

	@SuppressWarnings("unchecked")
	public void addRow(Object id, Object ... values) {
		if (id == null) {
			MLogUtil.log().d(this.getClass(),"addRow: id is null");
			return;
		}
		Item item = dataSource.addItem(id);
		if (item == null) {
			MLogUtil.log().d(this.getClass(),"addRow: item is null", id);
			return;
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] == null) {
				MLogUtil.log().d(this.getClass(),"addRow: column is null",i);
				return;
			}
			item.getItemProperty(
						columns[i].getId()
					).setValue(
							values.length > i ? 
									values[i] : 
										columns[i].getDefaultValue() 
								);
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean updateRow(Object id, Object[] values) {
		Item item = dataSource.getItem(id);
		if (item == null) return false;
		for (int i = 0; i < columns.length; i++) {
			item.getItemProperty(columns[i].getId()).setValue(values.length > i ? values[i] : columns[i].getDefaultValue() );
		}
		return true;
	}

	public void removeRow(Object id) {
		 dataSource.removeItem(id);
	}
	
}
