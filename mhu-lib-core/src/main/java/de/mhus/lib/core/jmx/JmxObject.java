package de.mhus.lib.core.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>JmxObject class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JmxObject extends MObject implements JmxObjectMBean {

	private boolean jmxRegistered = false;
	private ObjectName objectName;
	protected String jmxName = "Bean";
	protected String jmxType = "MObject";
	protected String jmxPackage = JmxObject.class.getCanonicalName();
	protected boolean jmxFixName = false;
	
	/**
	 * <p>Constructor for JmxObject.</p>
	 */
	public JmxObject() {
		jmxType = MString.afterLastIndex(getClass().getCanonicalName(), '.');
	}
	
	/**
	 * <p>getJmxObjectName.</p>
	 *
	 * @return a {@link javax.management.ObjectName} object.
	 * @throws javax.management.MalformedObjectNameException if any.
	 */
	public ObjectName getJmxObjectName() throws MalformedObjectNameException {
		if (objectName == null)
			objectName = createJmxObjectName();
		return objectName;
	}

	/**
	 * <p>createJmxObjectName.</p>
	 *
	 * @return a {@link javax.management.ObjectName} object.
	 * @throws javax.management.MalformedObjectNameException if any.
	 */
	protected ObjectName createJmxObjectName() throws MalformedObjectNameException {
		return new ObjectName(getJmxPackage() + ":name=" + getJmxName() + ",type=" + getJmxType());
	}

	/**
	 * <p>Getter for the field <code>jmxType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getJmxType() {
		return jmxType;
	}

	/**
	 * <p>Getter for the field <code>jmxPackage</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getJmxPackage() {
		return jmxPackage;
	}
	
	/**
	 * <p>Getter for the field <code>jmxName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getJmxName() {
		return jmxName;
	}
	
	/**
	 * <p>Setter for the field <code>jmxRegistered</code>.</p>
	 *
	 * @param b a boolean.
	 */
	public void setJmxRegistered(boolean b) {
		jmxRegistered = b;
	}
	
	/**
	 * <p>isJmxRegistered.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isJmxRegistered() {
		return jmxRegistered;
	}

	/**
	 * <p>Setter for the field <code>jmxName</code>.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 */
	public void setJmxName(String in) {
		if (isJmxRegistered()) return;
		if (jmxName.equals(in)) return;
		jmxName = in;
		objectName = null;
	}

	/**
	 * <p>Setter for the field <code>jmxPackage</code>.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 */
	public void setJmxPackage(String in) {
		if (isJmxRegistered()) return;
		if (jmxPackage.equals(in)) return;
		jmxPackage = in;
		objectName = null;
	}

	/**
	 * <p>isJmxFixName.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isJmxFixName() {
		return jmxFixName;
	}

	/**
	 * <p>setJmxProxy.</p>
	 *
	 * @param mBeanProxy a {@link de.mhus.lib.core.jmx.MBeanProxy} object.
	 */
	public void setJmxProxy(MBeanProxy mBeanProxy) {
		
	}

}
