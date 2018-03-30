/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

public abstract class JmxDescription {
	
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


	public abstract Object getAttribute(Object o, String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException;


	public abstract void setAttribute(Object o, Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException;
	
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


	public abstract Object invoke(Object o, String actionName, Object[] params,
			String[] signature) throws MBeanException, ReflectionException;


	public abstract MBeanInfo getMBeanInfo();
	
}
