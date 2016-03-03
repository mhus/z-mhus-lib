package de.mhus.lib.form;

import java.util.WeakHashMap;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>Abstract DataSource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class DataSource extends IProperties {

	/** Constant <code>CONNECTOR_TASK_DATA="data"</code> */
	public static final String CONNECTOR_TASK_DATA = "data";
//	public static final String CONNECTOR_TASK_VISIBLE  = "visible";
	/** Constant <code>CONNECTOR_TASK_ENABLED="enabled"</code> */
	public static final String CONNECTOR_TASK_ENABLED  = "enabled";
	/** Constant <code>CONNECTOR_TASK_TITLE="title"</code> */
	public static final String CONNECTOR_TASK_TITLE = "title";
	/** Constant <code>CONNECTOR_TASK_DESCRIPTION="description"</code> */
	public static final String CONNECTOR_TASK_DESCRIPTION = "description";
	/** Constant <code>CONNECTOR_TASK_ERROR="error"</code> */
	public static final String CONNECTOR_TASK_ERROR = "error";
	/** Constant <code>CONNECTOR_TASK_OPTIONS="options"</code> */
	public static final String CONNECTOR_TASK_OPTIONS = "options";
	/** Constant <code>PACKAGE_MEM="mem"</code> */
	public static final String PACKAGE_MEM = "mem";
	/** Constant <code>PACKAGE_PERSISTENT="p"</code> */
	public static final String PACKAGE_PERSISTENT = "p";
	/** Constant <code>NAME_INFORMATION="PACKAGE_MEM + .information"</code> */
	public static final String NAME_INFORMATION = PACKAGE_MEM + ".information";

	private boolean isConnected = false;
 	
	WeakHashMap<DataConnector, String> dataConnections = null;
	
	/**
	 * <p>createDataConnector.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 * @param srcConf a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @return a {@link de.mhus.lib.form.DataConnector} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public DataConnector createDataConnector(LayoutElement element, ResourceNode srcConf) throws MException {
		if (dataConnections == null) dataConnections = new WeakHashMap<DataConnector, String>();
		DataConnector dc = new DataConnector(this, element, srcConf);
		if (!isPropertyPossible(dc.getName())) return null;
		dataConnections.put(dc, "");
		return dc;
	}
	
	/**
	 * <p>fireValueChanged.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	protected void fireValueChanged(String name) {
		if (dataConnections == null) return;
		for (DataConnector dc : dataConnections.keySet())
			if (dc != null) 
				dc.fireDataChanged(name); 
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value){
		setPropertyData(key, MCast.objectToString(value) );
		if (isConnected) fireValueChanged(key);
	}

	/**
	 * <p>fireAll.</p>
	 */
	public void fireAll() {
		for (DataConnector dc : dataConnections.keySet())
			if (dc != null) 
				dc.fireDataChanged(null); 
	}
	
	/**
	 * <p>setPropertyData.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public abstract void setPropertyData(String name, Object value);

	/**
	 * <p>isConnected.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Set to connected if the source is direct used from the layout. This will prevent other cascaded
	 * sources to fire also events (wrong events) to the layout. This is only a soft rule.
	 *
	 * @param isConnected a boolean.
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	/**
	 * Return true if the property with this name is possible - maybe there is not value at the moment.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public abstract boolean isPropertyPossible(String name);
	
}
