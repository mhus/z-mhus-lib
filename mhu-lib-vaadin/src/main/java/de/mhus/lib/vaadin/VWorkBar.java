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

import java.util.List;
import java.util.UUID;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

import de.mhus.lib.core.util.Pair;

@SuppressWarnings("deprecation")
public abstract class VWorkBar extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	private Button bDelete;
	private Button bModify;
	private Button bAdd;
	private Label tStatus;
	private ComboBox menuDelete;
	private ComboBox menuModify;
	private ComboBox menuAdd;

	@SuppressWarnings("serial")
	public VWorkBar() {

		menuDelete = new ComboBox();
		menuDelete.setTextInputAllowed(false);
		menuDelete.setId("a" + UUID.randomUUID().toString().replace('-', 'x'));
		menuDelete.setWidth("0px");
		menuDelete.setNullSelectionAllowed(false);
		menuDelete.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				doMenuSelected();
			}
		});
		addComponent(menuDelete);
		
		bDelete = new Button(FontAwesome.MINUS);
		addComponent(bDelete);
		bDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doDelete();
			}
		});
		
		menuModify = menuDelete;
//		menuModify = new ComboBox();
//		menuModify.setTextInputAllowed(false);
//		menuModify.setId("a" + UUID.randomUUID().toString().replace('-', 'x'));
//		menuModify.setWidth("0px");
//		menuModify.setNullSelectionAllowed(false);
//		menuModify.addValueChangeListener(new Property.ValueChangeListener() {
//			
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				doMenuSelected();
//			}
//		});
//		addComponent(menuModify);
		
		bModify = new Button(FontAwesome.COG);
		addComponent(bModify);
		bModify.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doModify();
			}
		});

		menuAdd = menuDelete;
//		menuAdd = new ComboBox();
//		menuAdd.setTextInputAllowed(false);
//		menuAdd.setId("a" + UUID.randomUUID().toString().replace('-', 'x'));
//		menuAdd.setWidth("0px");
//		menuAdd.setNullSelectionAllowed(false);
//		menuAdd.addValueChangeListener(new Property.ValueChangeListener() {
//			
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				doMenuSelected();
//			}
//		});
//		addComponent(menuAdd);

		bAdd = new Button(FontAwesome.PLUS);
		addComponent(bAdd);
		bAdd.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doAdd();
			}
		});

		tStatus = new Label();
		addComponent(tStatus);
		setExpandRatio(tStatus, 1);
		
	}
	
	public void setButtonStyleName(String style) {
		bAdd.setStyleName(style);
		bModify.setStyleName(style);
		bDelete.setStyleName(style);
	}
	
	@SuppressWarnings("unchecked")
	protected void doMenuSelected() {
		Pair<String,Object[]> item = (Pair<String,Object[]>) menuDelete.getValue();
		if (item == null) return;
		Object[] val = item.getValue();
		if (((String)val[0]).equals("add"))
			doAdd(val[1]);
		else
		if (((String)val[0]).equals("mod"))
			doModify(val[1]);
		else
		if (((String)val[0]).equals("del"))
			doDelete(val[1]);
			
	}

	protected void doAdd() {
		List<Pair<String,Object>> options = getAddOptions();
		if (options == null || options.size() <= 0) return;
//		if (options.size() == 1) {
//			doAdd(options.get(0).getValue());
//		} else {
			
			menuAdd.removeAllItems();
			for (Pair<String, Object> item : options) {
				Pair<String, Object[]> out = new Pair<String, Object[]>(item.getKey(), new Object[] {"add", item.getValue()} );
				menuAdd.addItem(out);
			}
			String myCode = "$('#" + menuAdd.getId() + "').find('input')[0].click();";
			Page.getCurrent().getJavaScript().execute(myCode);
//		}
	}
	
	public abstract List<Pair<String, Object>> getAddOptions();

	public abstract List<Pair<String, Object>> getModifyOptions();
	
	public abstract List<Pair<String, Object>> getDeleteOptions();

	protected void doModify() {
		List<Pair<String,Object>> options = getModifyOptions();
		if (options == null || options.size() <= 0) return;
//		if (options.size() == 1) {
//			doModify(options.get(0).getValue());
//		} else {
			
			menuModify.removeAllItems();
			for (Pair<String, Object> item : options) {
				Pair<String, Object[]> out = new Pair<String, Object[]>(item.getKey(), new Object[] {"mod", item.getValue()} );
				menuModify.addItem(out);
			}
			String myCode = "$('#" + menuModify.getId() + "').find('input')[0].click();";
			Page.getCurrent().getJavaScript().execute(myCode);
//		}
	}

	protected void doDelete() {
		List<Pair<String,Object>> options = getDeleteOptions();
		if (options == null || options.size() <= 0) return;
//		if (options.size() == 1) {
//			doDelete(options.get(0).getValue());
//		} else {
			
			menuDelete.removeAllItems();
			for (Pair<String, Object> item : options) {
				Pair<String, Object[]> out = new Pair<String, Object[]>(item.getKey(), new Object[] {"del", item.getValue()} );
				menuDelete.addItem(out);
			}
			String myCode = "$('#" + menuDelete.getId() + "').find('input')[0].click();";
			Page.getCurrent().getJavaScript().execute(myCode);
//		}
	}

	protected abstract void doModify(Object action);

	protected abstract void doDelete(Object action);

	protected abstract void doAdd(Object action);

	public void setStatus(String msg) {
		tStatus.setCaption(msg);
	}
	
}
