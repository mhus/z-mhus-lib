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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;

import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

import de.mhus.lib.annotations.jmx.JmxManaged;

public class JmxDescritionAnnotations extends JmxDescriptionMBean {

	public JmxDescritionAnnotations(Object in) throws ClassNotFoundException,
			IntrospectionException {
		super(in);
	}

	@Override
	protected void analyse(Object in) throws ClassNotFoundException, IntrospectionException {
		
		String description = "";
		LinkedList<MBeanAttributeInfo> attributes = new LinkedList<MBeanAttributeInfo>();
		LinkedList<MBeanConstructorInfo> constructors = new LinkedList<MBeanConstructorInfo>();
		LinkedList<MBeanOperationInfo> operations = new LinkedList<MBeanOperationInfo>();
		LinkedList<MBeanNotificationInfo> notifications = new LinkedList<MBeanNotificationInfo>();

		Class<?> ifc = in.getClass();
		JmxManaged anno = null;
		do {
			anno = ifc.getAnnotation(JmxManaged.class);
			if (anno == null) ifc = ifc.getSuperclass();
		} while(anno == null);
		
		description = anno.descrition();
		HashSet<String> attr = new HashSet<String>();
		
		ifc = in.getClass();
		do {
			for (Method m : ifc.getMethods()) {
				String n = m.getName();
				anno = m.getAnnotation(JmxManaged.class);
				if (anno != null) {
					if (n.startsWith("get") && m.getReturnType() != void.class && m.getParameterTypes().length == 0 ) {
						String name = m.getName().substring(3);
						getter.put(name, m);
						attr.add(name);
					} else
					if (n.startsWith("is") && m.getReturnType() == boolean.class && m.getParameterTypes().length == 0) {
						String name = m.getName().substring(2);
						getter.put(name, m);
						attr.add(name);
					} else
					if (n.startsWith("set") && m.getReturnType() == void.class && m.getParameterTypes().length == 1) {
						String name = m.getName().substring(3);
						setter.put(name, m);
						attr.add(name);
					} else {
						methods.put(m.getName(), m);
						operations.add(new MBeanOperationInfo(anno.descrition(), m));
					}
				}
			}
			
			ifc = ifc.getSuperclass();
		} while (ifc != null);
		
		for (String name : attr) {
			Method g = getter.get(name);
			Method s = setter.get(name);
			attributes.add(new MBeanAttributeInfo(name, g != null ? g.getAnnotation(JmxManaged.class).descrition() : s.getAnnotation(JmxManaged.class).descrition() , g, s));
		}
		
		
		info = new MBeanInfo(in.getClass().getName(), description,
				attributes.toArray(new MBeanAttributeInfo[attributes.size()]), 
				constructors.toArray(new MBeanConstructorInfo[constructors.size()]), 
				operations.toArray(new MBeanOperationInfo[operations.size()]), 
				notifications.toArray(new MBeanNotificationInfo[notifications.size()]));
	
	}
}
