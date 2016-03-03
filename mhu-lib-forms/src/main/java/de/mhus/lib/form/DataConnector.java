package de.mhus.lib.form;

import java.util.Date;
import java.util.Observable;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

/**
 * <p>DataConnector class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DataConnector extends Observable {

	private DataSource dataSource;
	private ResourceNode config;
	private String name;
	private LayoutElement element;

	/**
	 * <p>Constructor for DataConnector.</p>
	 *
	 * @param dataSource a {@link de.mhus.lib.form.DataSource} object.
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 * @param srcConf a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public DataConnector(DataSource dataSource, LayoutElement element, ResourceNode srcConf) throws MException {
		this.dataSource = dataSource;
		this.config = srcConf;
		this.element = element;
		name = srcConf.getExtracted("name");
	}
	
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * The task of the connector, e.g. data enabled or visible.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTaskName() {
		try {
			return config.getName();
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getTaskName() + "=" + name;
	}

	/**
	 * <p>getString.</p>
	 *
	 * @param def a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public String getString(String def) throws MException {
		return dataSource.getString(name,def);
	}

	/**
	 * <p>getBoolean.</p>
	 *
	 * @param def a boolean.
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public boolean getBoolean(boolean def) throws MException {
		return dataSource.getBoolean(name, def);
	}

	/**
	 * <p>fireDataChanged.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void fireDataChanged(String name) {
		if (name != null && !getName().equals(name)) return;
		setChanged();
		notifyObservers();
	}

	/**
	 * <p>setString.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setString(String value) throws MException {
		if (!element.getFormControl().validate(element,this,value)) return;
		try {
			dataSource.setString(name, value);
			element.setErrorMessageDirect(null);
		} catch (DataValidationException dve) {
			dve.setErroMessage(element);
		}
	}

	/**
	 * <p>setBoolean.</p>
	 *
	 * @param value a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setBoolean(boolean value) throws MException {
		if (!element.getFormControl().validate(element,this,value)) return;
		try {
			dataSource.setBoolean(name, value);
			element.setErrorMessageDirect(null);
		} catch (DataValidationException dve) {
			dve.setErroMessage(element);
		}
	}

	/**
	 * <p>getObject.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Object getObject() throws MException {
		return dataSource.getProperty(name);
	}

	/**
	 * <p>setDate.</p>
	 *
	 * @param value a {@link java.util.Date} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
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

	/**
	 * <p>getDate.</p>
	 *
	 * @param def a {@link java.util.Date} object.
	 * @return a {@link java.util.Date} object.
	 */
	public Date getDate(Date def) {
		Date ret = dataSource.getDate(name);
		if (ret == null) return def;
		return ret;
	}

	/**
	 * <p>setNumber.</p>
	 *
	 * @param value a {@link java.lang.Number} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
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
	/**
	 * <p>getNumber.</p>
	 *
	 * @param def a {@link java.lang.Number} object.
	 * @return a {@link java.lang.Number} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Number getNumber(Number def) throws MException {
		return dataSource.getNumber(name,def);
	}

}
