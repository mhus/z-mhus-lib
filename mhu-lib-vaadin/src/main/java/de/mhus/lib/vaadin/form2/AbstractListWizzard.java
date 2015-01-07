package de.mhus.lib.vaadin.form2;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.form.control.AbstractWizzard;
import de.mhus.lib.form.control.WizzardCall;
import de.mhus.lib.vaadin.ColumnDefinition;
import de.mhus.lib.vaadin.FilterRequest;
import de.mhus.lib.vaadin.ModalDialog;
import de.mhus.lib.vaadin.SearchField;
import de.mhus.lib.vaadin.SimpleTable;

public abstract class AbstractListWizzard extends AbstractWizzard {

	private boolean showSearchField = true;

	@Override
	protected void doExecute(WizzardCall call) {
		UI window = ((UiVaadin)call.getElement().getUi()).getWindow();

		try {
			ListDialog dialog = new ListDialog(call);
			dialog.show(window);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private class ListDialog extends ModalDialog {

		private static final long serialVersionUID = 1L;
		private WizzardCall call;
		private ResourceNode options;
		private Action confirm;
		private Action cancel;
		private SimpleTable table;
		private SearchField filter;

		private ListDialog(WizzardCall call) throws Exception {
			this.call = call;
			this.options = call.getOptions();
			if (options == null) options = new HashConfig(); // empty config as default
			
			confirm = new Action("confirm", options.getExtracted("confirm", "OK"));
			cancel = new Action("cancel", options.getExtracted("cancel", "Cancel"));
			actions = new Action[] {confirm,cancel};
			initUI();
			setCaption(options.getExtracted("title",call.getElement().getTitle()));

		}
		
		@Override
		protected void initContent(VerticalLayout layout) throws Exception {
			if (options.isProperty("description")) {
				Label label = new Label(options.getExtracted("description",call.getElement().getTitle()));
				label.setContentMode(Label.CONTENT_XHTML);
				layout.addComponent(label);
			}

	        filter = new SearchField();
	        filter.setInputPrompt("Filter");
	        filter.setWidth("100%");
	        filter.setListener(new SearchField.Listener() {

				@Override
				public void doFilter(SearchField searchField) {
					ListDialog.this.doFilter();
				}
	        	
	        });
	        if (showSearchField) layout.addComponent(filter);
	        
			table = new SimpleTable();
			table.setSelectable(true);
			table.setMultiSelect(false);
			layout.addComponent(table);
			layout.setExpandRatio(table, 1.0f);
			
			table.addListener(new ItemClickEvent.ItemClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void itemClick(ItemClickEvent event) {
					if (event.isDoubleClick())
						confirm.doAction(ListDialog.this);
				}
			});
			table.createDataSource(AbstractListWizzard.this.createColumnDefinitions());
			AbstractListWizzard.this.fillTable(call, table, new FilterRequest(""));
		}

		protected void doFilter() {
			table.removeAllItems();
			AbstractListWizzard.this.fillTable(call, table, filter.createFilterRequest() );			
		}

		@Override
		protected boolean doAction(Action action) {
			if (action.equals(cancel)) return true;
			Object selected = table.getValue();
			return AbstractListWizzard.this.setSelected(call, selected);
		}

	}


	public boolean isShowSearchField() {
		return showSearchField;
	}

	public void setShowSearchField(boolean showSearchField) {
		this.showSearchField = showSearchField;
	}
	

	public abstract boolean setSelected(WizzardCall call, Object selected);

	public abstract ColumnDefinition[] createColumnDefinitions();
	
	public abstract void fillTable(WizzardCall call, SimpleTable table, FilterRequest filter);

}
