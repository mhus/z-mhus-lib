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

import java.util.Map;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.ui.Table;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MEventHandler;
import de.mhus.lib.core.MSystem;

/**
 * This type of table is able to expand the datasource. Therefore listen for the render events and expand the table if
 * <pre>
 * last &gt;= datasource.size() - 1
 * </pre>
 * It's also a need to catch the sort changed event. If you only show a small part of the real data you need to
 * send the sort order to the real data source and reset the data source if the order is changed and maybe
 * jump to the top of the table.
 * 
 * @author mikehummel
 *
 */
@SuppressWarnings("deprecation")
public class ExpandingTable extends Table {

	private static final long serialVersionUID = 1L;
	private String sortedColumn;
	private boolean sortedAscending;

	private MEventHandler<RenderListener> renderEventHandler = new MEventHandler<RenderListener>() {

		@Override
		public void onFire(RenderListener listener, Object event, Object... values) {
			listener.onRender(ExpandingTable.this, (Integer)values[0], (Integer)values[1]);
		}
		
	};
	private MEventHandler<SortListener> sortEventHandler = new MEventHandler<SortListener>() {

		@Override
		public void onFire(SortListener listener, Object event, Object... values) {
			listener.onSortChanged(ExpandingTable.this);
		}
		
	};

	public ExpandingTable() {
		super();
		initUI();
	}

	public ExpandingTable(String caption, Container dataSource) {
		super(caption, dataSource);
		initUI();
	}

	public ExpandingTable(String caption) {
		super(caption);
		initUI();
	}

	protected void initUI() {
		
        addHeaderClickListener(new Table.HeaderClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void headerClick(HeaderClickEvent event) {
				
				if (ExpandingTable.super.isSortDisabled()) return;
				
				String name = String.valueOf( event.getPropertyId() );
				if (name.equals(sortedColumn))
					sortedAscending = ! sortedAscending;
				else
					sortedAscending = true;
				sortedColumn = name;
				
				boolean changed = false;
				// update table now
				if ( !MSystem.equals(ExpandingTable.super.getSortContainerPropertyId(),sortedColumn)) {
					ExpandingTable.super.setSortContainerPropertyId(sortedColumn);
					changed = true;
				}
				if (ExpandingTable.super.isSortAscending() != sortedAscending) {
					ExpandingTable.super.setSortAscending(sortedAscending);
					changed = true;
				}
				
				if (changed)
					sortEventHandler.fire();
				
			}
		});

	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        
       // Notification.show("You are scrolling!\n " + variables);
       // System.out.println(variables);
        if (variables.containsKey("lastToBeRendered")) {
        	int last = MCast.toint(variables.get("lastToBeRendered"), -1);
        	int first = MCast.toint(variables.get("firstToBeRendered"), -1);
        	if (last >= 0) {
        		renderEventHandler.fire(null, first, last);
        	}
        }
    }
	
    public MEventHandler<RenderListener> renderEventHandler() {
		return renderEventHandler;
	}
	
    public MEventHandler<SortListener> sortEventHandler() {
		return sortEventHandler;
	}
    
	@Override
	public void setSortContainerPropertyId(Object propertyId) {
		sortedColumn = String.valueOf(propertyId);
		super.setSortContainerPropertyId(propertyId);
		sortEventHandler.fire();
	}

	@Override
	public void setSortAscending(boolean ascending) {
		sortedAscending = ascending;
		super.setSortAscending(ascending);
		sortEventHandler.fire();
	}

	@Override
	public void setSortDisabled(boolean sortDisabled) {
		if (sortDisabled != super.isSortDisabled()) {
			sortedColumn = null;
			sortedAscending = true;
			super.setSortDisabled(sortDisabled);
			sortEventHandler.fire();
		}
	}

	public String getSortedColumn() {
		return sortedColumn;
	}

	public boolean isSortedAscending() {
		return sortedAscending;
	}

	public static interface RenderListener {

		void onRender(ExpandingTable mhuTable, int first, int last);
		
	}

	public static interface SortListener {

		void onSortChanged(ExpandingTable mhuTable);
		
	}	
}
