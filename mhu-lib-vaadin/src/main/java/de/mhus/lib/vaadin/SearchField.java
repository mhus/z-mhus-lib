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

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;

import de.mhus.lib.core.util.FilterRequest;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;

@SuppressWarnings("deprecation")
public class SearchField extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	private ComboBox filter;
	private Listener listener;
	private Button bSearch;
	@SuppressWarnings("unused")
	private MNlsProvider nlsProvider;
//	private LinkedList<String> knownFacetNames = new LinkedList<>();

	@SuppressWarnings("serial")
	public SearchField(MNlsProvider nlsProvider) {
		this.nlsProvider = nlsProvider;
        filter = new ComboBox();
        filter.setNewItemsAllowed(true);
        filter.setNewItemHandler(new AbstractSelect.NewItemHandler() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void addNewItem(String newItemCaption) {
				addKnownFacetName(newItemCaption);
				filter.setValue(newItemCaption);
			}
		});
        filter.setInputPrompt(MNls.find(nlsProvider, "filter.prompt=Filter"));
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
        bSearch.setIcon(FontAwesome.SEARCH);
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
