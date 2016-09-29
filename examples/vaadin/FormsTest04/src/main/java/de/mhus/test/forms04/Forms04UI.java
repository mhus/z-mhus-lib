package de.mhus.test.forms04;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DummyDataSource;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.ModelDataSource;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.definition.FmCheckbox;
import de.mhus.lib.form.definition.FmDate;
import de.mhus.lib.form.definition.FmNumber;
import de.mhus.lib.form.definition.FmRichText;
import de.mhus.lib.form.definition.FmText;
import de.mhus.lib.form.definition.FmTextArea;
import de.mhus.lib.form.definition.FmDate.FORMATS;
import de.mhus.lib.form.definition.FmNumber.TYPES;
import de.mhus.lib.vaadin.ExpandingTable;
import de.mhus.lib.vaadin.MhuTable;
import de.mhus.lib.vaadin.form.VaadinForm;

@Title("Forms04")
@Theme("valo")
public class Forms04UI extends UI {

	private MyDataContainer data;

	@Override
	protected void init(VaadinRequest request) {
		
		
		try {
			
			MhuTable table = new MhuTable();
			
			table.renderEventHandler().register(new ExpandingTable.RenderListener() {
				
				@Override
				public void onRender(ExpandingTable mhuTable, int first, int last) {
		        	if (last >= 0 && last >= data.size()-1) {
		        		extendData();
		        	}
				}
			});
			table.sortEventHandler().register(new ExpandingTable.SortListener() {
				
				@Override
				public void onSortChanged(ExpandingTable mhuTable) {
					data.removeAllItems();
					System.out.println("sort changed: " + mhuTable.getSortedColumn() + " " + mhuTable.isSortedAscending());
	        		extendData();
	        		mhuTable.setCurrentPageFirstItemIndex(0); // scroll to top
				}
			});
			
	        table.setSizeFull();
	        table.addStyleName("borderless");
	        table.setSelectable(true);
	        table.setMultiSelect(true);
	        table.setColumnCollapsingAllowed(true);
	        table.setColumnReorderingAllowed(true);
	        table.setNullSelectionAllowed(true);

	        data = new MyDataContainer();
        	data.removeAllContainerFilters();
        	table.setContainerDataSource(data, getVisibleColumnOrder());
        	setTableHeaderNames(table);

			setContent(table);

			 extendData();
			
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void extendData() {
		for (int i = 0; i < 100; i++) {
			data.addBean(new MyData(data.size()));
		}
		System.out.println("Extended to " + data.size());
	}

	private LinkedList<String> getVisibleColumnOrder() {
		LinkedList<String> visible = new LinkedList<>();
		visible.add("id");
    	visible.add("name");
    	
		return visible;
	}
	
	private void setTableHeaderNames(MhuTable table) {
    	table.setColumnHeader("id", "Id");
    	table.setColumnHeader("name", "Name");
	}

	@WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = Forms04UI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
