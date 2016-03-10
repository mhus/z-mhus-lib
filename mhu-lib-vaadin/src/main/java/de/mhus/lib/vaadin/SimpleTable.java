package de.mhus.lib.vaadin;

import java.util.LinkedList;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;

import de.mhus.lib.core.MProperties;

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
		Item item = dataSource.addItem(id);
		for (int i = 0; i < columns.length; i++)
			item.getItemProperty(
						columns[i].getId()
					).setValue(
							values.length > i ? 
									values[i] : 
										columns[i].getDefaultValue() 
								);
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
