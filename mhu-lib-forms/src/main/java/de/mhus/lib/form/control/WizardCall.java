package de.mhus.lib.form.control;

import java.util.Date;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

/**
 * <p>WizardCall class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class WizardCall {

	private FormControl control;
	private LayoutElement element;

	/**
	 * <p>Constructor for WizardCall.</p>
	 *
	 * @param control a {@link de.mhus.lib.form.FormControl} object.
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	public WizardCall(FormControl control, LayoutElement element) {
		this.control = control;
		this.element = element;
	}

	/**
	 * <p>setString.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setString(String value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setString(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null);
	}
	
	/**
	 * <p>setDate.</p>
	 *
	 * @param value a {@link java.util.Date} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setDate(Date value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setDate(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null);
	}
	
	/**
	 * <p>setBoolean.</p>
	 *
	 * @param value a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setBoolean(boolean value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setBoolean(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null);
	}
	
	/**
	 * <p>setNumber.</p>
	 *
	 * @param value a {@link java.lang.Number} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setNumber(Number value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).setNumber(value);
		element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).fireDataChanged(null); 
	}

	/**
	 * <p>setEnabled.</p>
	 *
	 * @param value a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setEnabled(boolean value) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_ENABLED).setBoolean(value);
	}

	/**
	 * <p>setError.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setError(String msg) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_ERROR).setString(msg);
	}

	/**
	 * <p>setDescription.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setDescription(String msg) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_DESCRIPTION).setString(msg);
	}

	/**
	 * <p>setTitle.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void setTitle(String msg) throws MException {
		element.getDataConnector(DataSource.CONNECTOR_TASK_TITLE).setString(msg);
	}

	/**
	 * <p>getString.</p>
	 *
	 * @param def a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public String getString(String def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getString(def);
	}
	
	/**
	 * <p>getBoolean.</p>
	 *
	 * @param def a boolean.
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public boolean getBoolean(boolean def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getBoolean(def);
	}
	
	/**
	 * <p>getDate.</p>
	 *
	 * @param def a {@link java.util.Date} object.
	 * @return a {@link java.util.Date} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Date getDate(Date def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getDate(def);
	}
	
	/**
	 * <p>getNumber.</p>
	 *
	 * @param def a {@link java.lang.Number} object.
	 * @return a {@link java.lang.Number} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Number getNumber(Number def) throws MException {
		return element.getDataConnector(DataSource.CONNECTOR_TASK_DATA).getNumber(def);
	}

	/**
	 * <p>Getter for the field <code>control</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.FormControl} object.
	 */
	public FormControl getControl() {
		return control;
	}

	/**
	 * <p>Getter for the field <code>element</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	public LayoutElement getElement() {
		return element;
	}
	
	/**
	 * <p>getOptions.</p>
	 *
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public ResourceNode getOptions() {
		return element.getConfig().getNode("wizard");
	}

}
