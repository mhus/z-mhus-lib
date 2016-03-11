package de.mhus.lib.vaadin;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MEventHandler;
import de.mhus.lib.vaadin.MhuTable.RenderListener;

public class SimpleTable extends Table {

	private static final long serialVersionUID = 1L;
	private IndexedContainer dataSource;
	private ColumnDefinition[] columns;
	private MEventHandler<RenderListener> renderEventHandler = new MEventHandler<RenderListener>();

	public SimpleTable() {
		super();
		initUI();
	}

	public SimpleTable(String caption, Container dataSource) {
		super(caption, dataSource);
		initUI();
	}

	public SimpleTable(String caption) {
		super(caption);
		initUI();
	}

	protected void initUI() {
        setColumnReorderingAllowed(true);
        setColumnCollapsingAllowed(true);
        setSizeFull();
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
	
    public MEventHandler<RenderListener> renderEventHandler() {
		return renderEventHandler;
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
        		try {
	        		Method method = RenderListener.class.getMethod("onRender", SimpleTable.class, int.class, int.class);
	        		renderEventHandler.fire(method, this, first, last);
        		} catch (Throwable t) {
        			t.printStackTrace(); // should not happen
        		}
        	}
        }
    }
	
	public static interface RenderListener {

		void onRender(SimpleTable mhuTable, int first, int last);
		
	}

}
