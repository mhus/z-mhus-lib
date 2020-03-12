/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.jmx;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;

public class JmxDescriptionMBean extends JmxDescription {

    protected HashMap<String, Method> getter = new HashMap<String, Method>();
    protected HashMap<String, Method> setter = new HashMap<String, Method>();
    protected HashMap<String, Method> methods = new HashMap<String, Method>();
    protected MBeanInfo info;
    private String name;

    public JmxDescriptionMBean(Object in) throws ClassNotFoundException, IntrospectionException {
        name = in.getClass().getName();
        analyse(in);
    }

    protected void analyse(Object in) throws ClassNotFoundException, IntrospectionException {

        String description = "";
        LinkedList<MBeanAttributeInfo> attributes = new LinkedList<MBeanAttributeInfo>();
        LinkedList<MBeanConstructorInfo> constructors = new LinkedList<MBeanConstructorInfo>();
        LinkedList<MBeanOperationInfo> operations = new LinkedList<MBeanOperationInfo>();
        LinkedList<MBeanNotificationInfo> notifications = new LinkedList<MBeanNotificationInfo>();

        // load interface
        String ifcName = in.getClass().getName() + "MBean";
        Class<?> ifc = in.getClass().getClassLoader().loadClass(ifcName);

        // load
        HashSet<String> attr = new HashSet<String>();

        for (Method m : ifc.getMethods()) {
            String n = m.getName();
            if (n.startsWith("get")
                    && m.getReturnType() != void.class
                    && m.getParameterTypes().length == 0) {
                String name = m.getName().substring(3);
                getter.put(name, m);
                attr.add(name);
            } else if (n.startsWith("is")
                    && m.getReturnType() == boolean.class
                    && m.getParameterTypes().length == 0) {
                String name = m.getName().substring(2);
                getter.put(name, m);
                attr.add(name);
            } else if (n.startsWith("set")
                    && m.getReturnType() == void.class
                    && m.getParameterTypes().length == 1) {
                String name = m.getName().substring(3);
                setter.put(name, m);
                attr.add(name);
            } else {
                methods.put(m.getName(), m);
                operations.add(new MBeanOperationInfo("", m));
            }
        }

        for (String name : attr) {
            Method g = getter.get(name);
            Method s = setter.get(name);
            attributes.add(new MBeanAttributeInfo(name, "", g, s));
        }

        info =
                new MBeanInfo(
                        in.getClass().getName(),
                        description,
                        attributes.toArray(new MBeanAttributeInfo[attributes.size()]),
                        constructors.toArray(new MBeanConstructorInfo[constructors.size()]),
                        operations.toArray(new MBeanOperationInfo[operations.size()]),
                        notifications.toArray(new MBeanNotificationInfo[notifications.size()]));
    }

    public Method getGetter(String attribute) {
        return getter.get(attribute);
    }

    public Method getSetter(String attribute) {
        return setter.get(attribute);
    }

    public Method getMethod(String actionName) {
        return methods.get(actionName);
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return info;
    }

    @Override
    public Object getAttribute(Object o, String attribute)
            throws AttributeNotFoundException, MBeanException, ReflectionException {
        Method method = getGetter(attribute);
        if (method == null) throw new AttributeNotFoundException(attribute + "@" + name);
        try {
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            throw new ReflectionException(e, attribute + "@" + name);
        }
    }

    @Override
    public void setAttribute(Object o, Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException,
                    ReflectionException {

        Method method = getSetter(attribute.getName());
        if (method == null) throw new AttributeNotFoundException(attribute + "@" + name);
        try {
            method.invoke(o, new Object[] {attribute.getValue()});
        } catch (Exception e) {
            throw new ReflectionException(e, attribute + "@" + name);
        }
    }

    @Override
    public Object invoke(Object o, String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {

        Method method = getMethod(actionName);
        // if (method == null) throw new ReflectionException("" + actionName + "@" + name);

        try {
            return method.invoke(o, params);
        } catch (Exception e) {
            throw new ReflectionException(e, actionName + "@" + name);
        }
    }
}
