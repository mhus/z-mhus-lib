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
