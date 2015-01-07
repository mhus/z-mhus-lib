package de.mhus.lib.form;

import java.util.WeakHashMap;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public abstract class DataSource extends IProperties {

	public static final String CONNECTOR_TASK_DATA = "data";
//	public static final String CONNECTOR_TASK_VISIBLE  = "visible";
	public static final String CONNECTOR_TASK_ENABLED  = "enabled";
	public static final String CONNECTOR_TASK_TITLE = "title";
	public static final String CONNECTOR_TASK_DESCRIPTION = "description";
	public static final String CONNECTOR_TASK_ERROR = "error";
	public static final String CONNECTOR_TASK_OPTIONS = "options";
	public static final String PACKAGE_MEM = "mem";
	public static final String PACKAGE_PERSISTENT = "p";
	public static final String NAME_INFORMATION = PACKAGE_MEM + ".information";

	private boolean isConnected = false;
 	
	WeakHashMap<DataConnector, String> dataConnections = null;
	
	public DataConnector createDataConnector(LayoutElement element, ResourceNode srcConf) throws MException {
		if (dataConnections == null) dataConnections = new WeakHashMap<DataConnector, String>();
		DataConnector dc = new DataConnector(this, element, srcConf);
		if (!isPropertyPossible(dc.getName())) return null;
		dataConnections.put(dc, "");
		return dc;
	}
	
	protected void fireValueChanged(String name) {
		if (dataConnections == null) return;
		for (DataConnector dc : dataConnections.keySet())
			if (dc != null) 
				dc.fireDataChanged(name); 
	}

	@Override
	public void setProperty(String key, Object value){
		setPropertyData(key, MCast.objectToString(value) );
		if (isConnected) fireValueChanged(key);
	}

	public void fireAll() {
		for (DataConnector dc : dataConnections.keySet())
			if (dc != null) 
				dc.fireDataChanged(null); 
	}
	
	public abstract void setPropertyData(String name, Object value);

	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Set to connected if the source is direct used from the layout. This will prevent other cascaded
	 * sources to fire also events (wrong events) to the layout. This is only a soft rule.
	 * 
	 * @param isConnected
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	/**
	 * Return true if the property with this name is possible - maybe there is not value at the moment.
	 * 
	 * @param name
	 * @return
	 */
	public abstract boolean isPropertyPossible(String name);
	
}
