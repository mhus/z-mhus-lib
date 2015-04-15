package de.mhus.lib.vaadin;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class SearchField extends HorizontalLayout {

	private TextField filter;
	private Listener listener;
	private Button bSearch;

	public SearchField() {
        filter = new TextField();
        filter.setInputPrompt("Filter");
        filter.setImmediate(true);
        filter.addShortcutListener(new ShortcutListener("Filter",ShortcutAction.KeyCode.ENTER, null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				if (target == filter)
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
        
        bSearch = new Button("X");
        bSearch.addListener(new Button.ClickListener() {
			
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


	public static interface Listener {

		void doFilter(SearchField searchField);
		
	}
}
