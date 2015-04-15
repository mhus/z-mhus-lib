package de.mhus.lib.form;

import java.util.Date;
import java.util.Observable;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

public class DataConnector extends Observable {

	private DataSource dataSource;
	private ResourceNode config;
	private String name;
	private LayoutElement element;

	public DataConnector(DataSource dataSource, LayoutElement element, ResourceNode srcConf) throws MException {
		this.dataSource = dataSource;
		this.config = srcConf;
		this.element = element;
		name = srcConf.getExtracted("name");
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * The task of the connector, e.g. data enabled or visible.
	 * @return
	 */
	public String getTaskName() {
		try {
			return config.getName();
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		return getTaskName() + "=" + name;
	}

	public String getString(String def) throws MException {
		return dataSource.getString(name,def);
	}

	public boolean getBoolean(boolean def) throws MException {
		return dataSource.getBoolean(name, def);
	}

	public void fireDataChanged(String name) {
		if (name != null && !getName().equals(name)) return;
		setChanged();
		notifyObservers();
	}

	public void setString(String value) throws MException {
		if (!element.getFormControl().validate(element,this,value)) return;
		try {
			dataSource.setString(name, value);
			element.setErrorMessageDirect(null);
		} catch (DataValidationException dve) {
			dve.setErroMessage(element);
		}
	}

	public void setBoolean(boolean value) throws MException {
		if (!element.getFormControl().validate(element,this,value)) return;
		try {
			dataSource.setBoolean(name, value);
			element.setErrorMessageDirect(null);
		} catch (DataValidationException dve) {
			dve.setErroMessage(element);
		}
	}

	public Object getObject() throws MException {
		return dataSource.getProperty(name);
	}

	public void setDate(Date value) throws MException {
		if (!element.getFormControl().validate(element,this,value)) return;
		try {
			dataSource.setDate(name, value);
			element.setErrorMessageDirect(null);
		} catch (DataValidationException dve) {
			dve.setErroMessage(element);
		} catch (Exception e) {
			element.setErrorMessageDirect(e.toString());
		}
	}

	public Date getDate(Date def) {
		Date ret = dataSource.getDate(name);
		if (ret == null) return def;
		return ret;
	}

	public void setNumber(Number value) throws MException {
		if (!element.getFormControl().validate(element,this,value)) return;
		try {
			dataSource.setNumber(name, value);
			element.setErrorMessageDirect(null);
		} catch (DataValidationException dve) {
			dve.setErroMessage(element);
		} catch (Exception e) {
			element.setErrorMessageDirect(e.toString());
		}
	}

	// TODO move into mhu-lib
	public Number getNumber(Number def) throws MException {
		return dataSource.getNumber(name,def);
	}

}
