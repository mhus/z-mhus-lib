/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.jmx;

import java.lang.ref.WeakReference;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import de.mhus.lib.core.util.MObject;

final class MBeanProxy extends MObject implements DynamicMBean {

    /** The real MBean object. */
    private final ReferenceImpl weak;

    private MRemoteManager server;
    private ObjectName name;
    //	private Object real;
    private JmxDescription desc;

    public MBeanProxy(
            Object realObject,
            JmxDescription desc,
            MRemoteManager server,
            ObjectName name,
            boolean weak)
            throws Exception {
        // have tow references, the real only to hold the project for a while, depends on the
        // strategy
        this.weak = new ReferenceImpl(realObject);
        //        if (!weak) this.real = realObject;
        this.desc = desc;
        if (desc == null) this.desc = JmxDescription.create(realObject);
        this.server = server;
        this.name = name;
        if (realObject instanceof JmxObject) {
            ((JmxObject) realObject).setJmxProxy(this);
        }
    }

    private class ReferenceImpl extends WeakReference<Object> {
        public ReferenceImpl(Object referent) {
            super(referent);
        }
    }

    @Override
    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException, ReflectionException {

        return desc.getAttribute(getObject(), attribute);
    }

    private Object getObject() {
        Object out = weak.get();
        if (out == null) {
            unregsiter();
            throw new IllegalStateException(name + " no longer exists");
        }
        return out;
    }

    private synchronized void unregsiter() {
        if (server == null) return;
        server.unregister(name);
        server = null;
    }

    @Override
    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException,
                    ReflectionException {

        desc.setAttribute(getObject(), attribute);
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return desc.getAttributes(getObject(), attributes);
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return desc.setAttributes(getObject(), attributes);
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {
        return desc.invoke(getObject(), actionName, params, signature);
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return desc.getMBeanInfo();
    }

    public void setName(ObjectName objectName) {
        name = objectName;
    }

    public void check() {
        try {
            getObject();
        } catch (Throwable t) {
            log().t("check failed", name);
        }
    }
}
