package de.mhus.lib.vaadin.form2;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.form.control.AbstractWwizard;
import de.mhus.lib.form.control.WwizardCall;
import de.mhus.lib.vaadin.ColumnDefinition;
import de.mhus.lib.vaadin.FilterRequest;
import de.mhus.lib.vaadin.ModalDialog;
import de.mhus.lib.vaadin.SearchField;
import de.mhus.lib.vaadin.SimpleTable;

public abstract class AbstractListWwizard extends AbstractWwizard {

	private boolean showSearchField = true;

	@Override
	protected void doExecute(WwizardCall call) {
		Window window = ((UiVaadin)call.getElement().getUi()).getWindow();

		try {
			ListDialog dialog = new ListDialog(call);
			dialog.show(window);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private class ListDialog extends ModalDialog {

		private static final long serialVersionUID = 1L;
		private WwizardCall call;
		private ResourceNode options;
		private Action confirm;
		private Action cancel;
		private SimpleTable table;
		private SearchField filter;

		private ListDialog(WwizardCall call) throws Exception {
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
			table.createDataSource(AbstractListWwizard.this.createColumnDefinitions());
			AbstractListWwizard.this.fillTable(call, table, new FilterRequest(""));
		}

		protected void doFilter() {
			table.removeAllItems();
			AbstractListWwizard.this.fillTable(call, table, filter.createFilterRequest() );			
		}

		@Override
		protected boolean doAction(Action action) {
			if (action.equals(cancel)) return true;
			Object selected = table.getValue();
			return AbstractListWwizard.this.setSelected(call, selected);
		}

	}


	public boolean isShowSearchField() {
		return showSearchField;
	}

	public void setShowSearchField(boolean showSearchField) {
		this.showSearchField = showSearchField;
	}
	

	public abstract boolean setSelected(WwizardCall call, Object selected);

	public abstract ColumnDefinition[] createColumnDefinitions();
	
	public abstract void fillTable(WwizardCall call, SimpleTable table, FilterRequest filter);

}
