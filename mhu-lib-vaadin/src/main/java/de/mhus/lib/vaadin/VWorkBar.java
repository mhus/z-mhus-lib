package de.mhus.lib.vaadin;

import java.util.List;
import java.util.UUID;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;

import de.mhus.lib.core.util.Pair;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;

public abstract class VWorkBar extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	private Button bDelete;
	private Button bModify;
	private Button bAdd;
	private Label tStatus;
	private ComboBox menu;

	public VWorkBar() {

		menu = new ComboBox();
		menu.setTextInputAllowed(false);
		menu.setId("a" + UUID.randomUUID().toString().replace('-', 'x'));
		menu.setWidth("0px");
		menu.setNullSelectionAllowed(false);
		menu.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				doMenuSelected();
			}
		});
		addComponent(menu);
		
		bDelete = new Button(FontAwesome.MINUS);
		addComponent(bDelete);
		bDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doDelete();
			}
		});
		bModify = new Button(FontAwesome.COG);
		addComponent(bModify);
		bModify.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				doModify();
			}
		});
		
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
	
	protected void doMenuSelected() {
		Pair<String,String> item = (Pair<String, String>) menu.getValue();
		if (item == null) return;
		String val = item.getValue();
		if (val.startsWith("add:"))
			doAdd(val.substring(4));
		else
		if (val.startsWith("mod:"))
			doModify(val.substring(4));
		else
		if (val.startsWith("del:"))
			doDelete(val.substring(4));
			
	}

	protected void doAdd() {
		List<Pair<String,String>> options = getAddOptions();
		if (options == null || options.size() <= 0) return;
		if (options.size() == 1) {
			doAdd(options.get(0).getValue());
		} else {
			
			menu.removeAllItems();
			for (Pair<String, String> item : options) {
				item = new Pair<String, String>(item.getKey(), "add:" + item.getValue());
				menu.addItem(item);
			}
			String myCode = "$('#" + menu.getId() + "').find('input')[0].click();";
			Page.getCurrent().getJavaScript().execute(myCode);
		}
	}
	
	public abstract List<Pair<String, String>> getAddOptions();

	public abstract List<Pair<String, String>> getModifyOptions();
	
	public abstract List<Pair<String, String>> getDeleteOptions();

	protected void doModify() {
		List<Pair<String,String>> options = getModifyOptions();
		if (options == null || options.size() <= 0) return;
		if (options.size() == 1) {
			doModify(options.get(0).getValue());
		} else {
			
			menu.removeAllItems();
			for (Pair<String, String> item : options) {
				item = new Pair<String, String>(item.getKey(), "mod:" + item.getValue());
				menu.addItem(item);
			}
			String myCode = "$('#" + menu.getId() + "').find('input')[0].click();";
			Page.getCurrent().getJavaScript().execute(myCode);
		}
	}

	protected void doDelete() {
		List<Pair<String,String>> options = getModifyOptions();
		if (options == null || options.size() <= 0) return;
		if (options.size() == 1) {
			doDelete(options.get(0).getValue());
		} else {
			
			menu.removeAllItems();
			for (Pair<String, String> item : options) {
				item = new Pair<String, String>(item.getKey(), "del:" + item.getValue());
				menu.addItem(item);
			}
			String myCode = "$('#" + menu.getId() + "').find('input')[0].click();";
			Page.getCurrent().getJavaScript().execute(myCode);
		}
	}

	protected abstract void doModify(String action);

	protected abstract void doDelete(String action);

	protected abstract void doAdd(String action);

	public void setStatus(String msg) {
		tStatus.setCaption(msg);
	}
	
}
