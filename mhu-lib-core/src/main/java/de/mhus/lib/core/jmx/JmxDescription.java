package de.mhus.lib.core.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

import de.mhus.lib.annotations.jmx.JmxManaged;

/**
 * <p>Abstract JmxDescription class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class JmxDescription {
	
	/**
	 * <p>create.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.jmx.JmxDescription} object.
	 * @throws java.lang.Exception if any.
	 */
	public static JmxDescription create(Object in) throws Exception {
		if (in  instanceof DynamicMBean) {
			return new JmxDescriptionDynamic(in);
		} else {
			Class<?> current = in.getClass();
			do {
				if (current.isAnnotationPresent(JmxManaged.class)){
					return new JmxDescritionAnnotations(in);
				}
				current = current.getSuperclass();
			} while (current != null);
		}
		return new JmxDescriptionMBean(in);
	}


	/**
	 * <p>getAttribute.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @param attribute a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws javax.management.AttributeNotFoundException if any.
	 * @throws javax.management.MBeanException if any.
	 * @throws javax.management.ReflectionException if any.
	 */
	public abstract Object getAttribute(Object o, String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException;


	/**
	 * <p>setAttribute.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @param attribute a {@link javax.management.Attribute} object.
	 * @throws javax.management.AttributeNotFoundException if any.
	 * @throws javax.management.InvalidAttributeValueException if any.
	 * @throws javax.management.MBeanException if any.
	 * @throws javax.management.ReflectionException if any.
	 */
	public abstract void setAttribute(Object o, Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException;
	
	/**
	 * <p>getAttributes.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @param attributes an array of {@link java.lang.String} objects.
	 * @return a {@link javax.management.AttributeList} object.
	 */
	public AttributeList getAttributes(Object o, String[] attributes) {
		AttributeList list = new AttributeList();
        for (String name : attributes) {
			try {
				Object value = getAttribute(o, name);
	            if (value != null)
	                list.add(new Attribute(name, value));
			} catch (Exception e) {
			}
        }
        return list;
	}

	/**
	 * <p>setAttributes.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @param attributes a {@link javax.management.AttributeList} object.
	 * @return a {@link javax.management.AttributeList} object.
	 */
	public AttributeList setAttributes(Object o, AttributeList attributes) {
		Attribute[] attrs = (Attribute[]) attributes.toArray(new Attribute[0]);
        AttributeList retlist = new AttributeList();
        for (Attribute attr : attrs) {
            String name = attr.getName();
            Object value = attr.getValue();
            try {
            	setAttribute(o, attr);
                retlist.add(new Attribute(name, value));
            } catch (Exception e) {
			}
        }
        return retlist;
	}


	/**
	 * <p>invoke.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @param actionName a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param signature an array of {@link java.lang.String} objects.
	 * @return a {@link java.lang.Object} object.
	 * @throws javax.management.MBeanException if any.
	 * @throws javax.management.ReflectionException if any.
	 */
	public abstract Object invoke(Object o, String actionName, Object[] params,
			String[] signature) throws MBeanException, ReflectionException;


	/**
	 * <p>getMBeanInfo.</p>
	 *
	 * @return a {@link javax.management.MBeanInfo} object.
	 */
	public abstract MBeanInfo getMBeanInfo();
	
}
