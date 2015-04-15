package de.mhus.lib.vaadin.form2;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;
import de.mhus.lib.form.DataSource;

public class UiInformation extends UiVaadin {

	private Panel field;
	private Label description;

	@Override
	public void doUpdate(DataConnector data) throws MException {
		if (field == null) return;
		if (data.getTaskName().equals(DataSource.CONNECTOR_TASK_DATA)) {
			description.setValue(data.getString("sdf"));
			return;
		}
	}
	
	@Override
	protected Component getField() {
		if (field == null) {
			field = new Panel();
			field.setWidth("100%");
			field.setHeight("100px");
			description = new Label();
			description.setContentMode(Label.CONTENT_XHTML);
			//description.setWidth("100%");
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(description);
			layout.setWidth("100%");
			field.setContent(layout);
		}
		return field;
	}

	@Override
	protected void doConnect() {
	}

	@Override
	protected void doDisconnect() {
	}

	@Override
	public boolean isInformationElement() {
		return true;
	}
	
}
