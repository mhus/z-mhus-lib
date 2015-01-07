package de.mhus.lib.core.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

public class JmxDescriptionDynamic extends JmxDescription {

	private MBeanInfo info;

	public JmxDescriptionDynamic(Object in) {
		info = ((DynamicMBean)in).getMBeanInfo();
	}

	@Override
	public Object getAttribute(Object o, String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		return ((DynamicMBean)o).getAttribute(attribute);
	}

	@Override
	public void setAttribute(Object o, Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		((DynamicMBean)o).setAttribute(attribute);		
	}

	@Override
	public Object invoke(Object o, String actionName, Object[] params,
			String[] signature) throws MBeanException, ReflectionException {
		return ((DynamicMBean)o).invoke(actionName, params, signature);
	}

	@Override
	public AttributeList getAttributes(Object o, String[] attributes) {
		return ((DynamicMBean)o).getAttributes(attributes);
	}

	@Override
	public AttributeList setAttributes(Object o, AttributeList attributes) {
		return ((DynamicMBean)o).setAttributes(attributes);
	}
	
	@Override
	public MBeanInfo getMBeanInfo() {
		return info;
	}

}
