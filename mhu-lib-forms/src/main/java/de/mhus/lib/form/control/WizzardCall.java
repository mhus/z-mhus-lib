package de.mhus.lib.form.control;

import java.util.Date;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

public class WizzardCall {

	private FormControl control;
	private LayoutElement element;

	public WizzardCall(FormControl control, LayoutElement element) {
		this.control = control;
		this.element = element;
	}

	public void setString(String value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setString(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null);
	}
	
	public void setDate(Date value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setDate(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null);
	}
	
	public void setBoolean(boolean value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setBoolean(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null);
	}
	
	public void setNumber(Number value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setNumber(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null); 
	}

	public void setEnabled(boolean value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_ENABLED).setBoolean(value);
	}

	public void setError(String msg) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_ERROR).setString(msg);
	}

	public void setDescription(String msg) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DESCRIPTION).setString(msg);
	}

	public void setTitle(String msg) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_TITLE).setString(msg);
	}

	public String getString(String def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getString(def);
	}
	
	public boolean getBoolean(boolean def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getBoolean(def);
	}
	
	public Date getDate(Date def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getDate(def);
	}
	
	public Number getNumber(Number def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getNumber(def);
	}

	public FormControl getControl() {
		return control;
	}

	public LayoutElement getElement() {
		return element;
	}
	
	public ResourceNode getOptions() {
		return element.getConfig().getNode("wizzard");
	}

}
