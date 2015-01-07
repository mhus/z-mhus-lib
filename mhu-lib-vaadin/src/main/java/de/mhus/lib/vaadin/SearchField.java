package de.mhus.lib.vaadin;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;

public class SearchField extends HorizontalLayout {

	private ComboBox filter;
	private Listener listener;
	private Button bSearch;
//	private LinkedList<String> knownFacetNames = new LinkedList<>();

	public SearchField() {
        filter = new ComboBox();
        filter.setNewItemsAllowed(true);
        filter.setNewItemHandler(new AbstractSelect.NewItemHandler() {
			
			@Override
			public void addNewItem(String newItemCaption) {
				addKnownFacetName(newItemCaption);
				filter.setValue(newItemCaption);
			}
		});
        filter.setInputPrompt("Filter");
        filter.setImmediate(true);
//        filter.addShortcutListener(new ShortcutListener("Filter",ShortcutAction.KeyCode.ENTER, null) {
//			
//			@Override
//			public void handleAction(Object sender, Object target) {
//				if (target == filter)
//					doFilter();
//			}
//		});
        filter.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				doFilter();
			}
		});
        
//        filter.addListener(new FieldEvents.TextChangeListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void textChange(TextChangeEvent event) {
//				doFilter();
//			}
//        	
//        });
        addComponent(filter);
        setExpandRatio(filter, 1);
        filter.setWidth("100%");
        
        bSearch = new Button();
        bSearch.setStyleName("icon-search");
        bSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doFilter();
			}
		});

        addComponent(bSearch);
        setExpandRatio(bSearch, 0);
        setWidth("100%");
        
        
        
        
//        bReload = new Button()

	}

	protected void doFilter() {
		if (listener != null) listener.doFilter(this);
	}

	public void setInputPrompt(String prompt) {
		filter.setInputPrompt(prompt);
	}
	
	public void setValue(String value) {
		filter.setValue(value);
	}
	
	public FilterRequest createFilterRequest() {
		return new FilterRequest( (String)filter.getValue() );
	}
	
	public Listener getListener() {
		return listener;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void addKnownFacetName(String name) {
		if (!filter.containsId(name)) {
			filter.addItem(name);
		}
	}

	public static interface Listener {

		void doFilter(SearchField searchField);
		
	}
	
}
