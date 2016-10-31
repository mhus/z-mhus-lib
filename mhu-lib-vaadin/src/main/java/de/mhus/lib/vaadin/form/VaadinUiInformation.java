package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.form.UiInformation;

public class VaadinUiInformation extends Panel implements UiInformation {

	private Label description;

	public VaadinUiInformation() {
		initUI();
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

	@Override
	public void setInformation(String name, String description) {
		if (this.description == null) return;
		this.description.setValue("<b>" + name + "</b><br/>" + description);
	}
	
}
