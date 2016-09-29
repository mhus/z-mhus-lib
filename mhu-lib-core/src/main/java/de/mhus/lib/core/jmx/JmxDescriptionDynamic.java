package de.mhus.lib.core.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/**
 * <p>JmxDescriptionDynamic class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JmxDescriptionDynamic extends JmxDescription {

	private MBeanInfo info;

	/**
	 * <p>Constructor for JmxDescriptionDynamic.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public JmxDescriptionDynamic(Object in) {
		info = ((DynamicMBean)in).getMBeanInfo();
	}

	/** {@inheritDoc} */
	@Override
	public Object getAttribute(Object o, String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		return ((DynamicMBean)o).getAttribute(attribute);
	}

	/** {@inheritDoc} */
	@Override
	public void setAttribute(Object o, Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		((DynamicMBean)o).setAttribute(attribute);		
	}

	/** {@inheritDoc} */
	@Override
	public Object invoke(Object o, String actionName, Object[] params,
			String[] signature) throws MBeanException, ReflectionException {
		return ((DynamicMBean)o).invoke(actionName, params, signature);
	}

	/** {@inheritDoc} */
	@Override
	public AttributeList getAttributes(Object o, String[] attributes) {
		return ((DynamicMBean)o).getAttributes(attributes);
	}

	/** {@inheritDoc} */
	@Override
	public AttributeList setAttributes(Object o, AttributeList attributes) {
		return ((DynamicMBean)o).setAttributes(attributes);
	}
	
	/** {@inheritDoc} */
	@Override
	public MBeanInfo getMBeanInfo() {
		return info;
	}

}
