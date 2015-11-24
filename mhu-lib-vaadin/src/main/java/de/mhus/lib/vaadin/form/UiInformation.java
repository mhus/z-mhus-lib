package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;

public class UiInformation extends Panel {

	private Label description;

	public void doUpdate(String desc) throws MException {
		description.setValue(desc);
	}
	
	protected void initUI() {
		setWidth("100%");
		setHeight("100px");
		description = new Label();
		description.setContentMode(Label.CONTENT_XHTML);
		//description.setWidth("100%");
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(description);
		layout.setWidth("100%");
		setContent(layout);
	}
	
}
