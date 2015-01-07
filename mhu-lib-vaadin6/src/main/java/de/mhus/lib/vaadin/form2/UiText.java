package de.mhus.lib.vaadin.form2;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;
import de.mhus.lib.form.DataSource;

public class UiText extends UiVaadin {

	protected AbstractField field;

	@Override
	protected void doConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doDisconnect() {
		// TODO Auto-generated method stub
		
	}

	protected Object getValueFromDataSource(DataConnector data) throws MException {
		return data.getString("");
	}
	
	protected void setValueToDataSource(DataConnector data) throws MException {
		data.setString((String)field.getValue());
	}
	
	protected void setValueToField(Object arg) {
		field.setValue(arg);
	}
	
	protected Object getValueFromField() {
		return field.getValue();
	}
	
	protected void doUpdateData(DataConnector data) throws MException {
		Object arg = getValueFromDataSource(data);
		if (equals(arg,getValueFromField())) return;
		setValueToField(arg);
	}
	
	protected void doUpdateEnabled(DataConnector data) throws MException {
		boolean arg = data.getBoolean(field.isEnabled());
		if (equals(arg,field.isEnabled())) return;
		field.setEnabled(data.getBoolean(true));
	}
	
	protected void doUpdateDescription(DataConnector data) throws MException {
		if (field instanceof AbstractTextField) {
			String arg = data.getString((String)((AbstractTextField)field).getInputPrompt());
			if (equals(arg,((AbstractTextField)field).getInputPrompt())) return;
			((AbstractTextField)field).setInputPrompt(arg);
		}
	}
	
	@Override
	public void doUpdate(DataConnector data) throws MException {
		if (field == null) return;
		if (data.getTaskName().equals(DataSource.CONNECTOR_TASK_DATA)) {
			doUpdateData(data);
			return;
		}
		
		if (data.getTaskName().equals(DataSource.CONNECTOR_TASK_ENABLED)) {
			doUpdateEnabled(data);
			return;
		}
				
		if (data.getTaskName().equals(DataSource.CONNECTOR_TASK_DESCRIPTION)) {
			doUpdateDescription(data);
			return;
		}
				
		super.doUpdate(data);
	}

	@Override
	protected Component getField() throws MException {
		if (field == null) {
			field = createTextField();
			field.setWidth("100%");
			if (field instanceof AbstractTextField)
				((AbstractTextField)field).setInputPrompt(getElement().getDescription());
			getElement().fireAllDataSources();
			
			field.addListener(new Property.ValueChangeListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					try {
						setValueToDataSource(getElement().getDataConnector(DataSource.CONNECTOR_TASK_DATA));
					} catch (MException e) {
						e.printStackTrace();
					}
				}
			});
			
			prepareInputField(field);
		}
		return field;
	}

	protected AbstractField createTextField() throws MException {
		return new TextField();
	}

}
