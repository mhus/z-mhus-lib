/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.MObject;

public class JmxObject extends MObject implements JmxObjectMBean {

    private boolean jmxRegistered = false;
    private ObjectName objectName;
    protected String jmxName = "Bean";
    protected String jmxType = "MObject";
    protected String jmxPackage = JmxObject.class.getCanonicalName();
    protected boolean jmxFixName = false;

    public JmxObject() {
        jmxType = MString.afterLastIndex(getClass().getCanonicalName(), '.');
    }

    public ObjectName getJmxObjectName() throws MalformedObjectNameException {
        if (objectName == null) objectName = createJmxObjectName();
        return objectName;
    }

    protected ObjectName createJmxObjectName() throws MalformedObjectNameException {
        return new ObjectName(getJmxPackage() + ":name=" + getJmxName() + ",type=" + getJmxType());
    }

    protected String getJmxType() {
        return jmxType;
    }

    protected String getJmxPackage() {
        return jmxPackage;
    }

    protected String getJmxName() {
        return jmxName;
    }

    public void setJmxRegistered(boolean b) {
        jmxRegistered = b;
    }

    public boolean isJmxRegistered() {
        return jmxRegistered;
    }

    public void setJmxName(String in) {
        if (isJmxRegistered()) return;
        if (jmxName.equals(in)) return;
        jmxName = in;
        objectName = null;
    }

    public void setJmxPackage(String in) {
        if (isJmxRegistered()) return;
        if (jmxPackage.equals(in)) return;
        jmxPackage = in;
        objectName = null;
    }

    public boolean isJmxFixName() {
        return jmxFixName;
    }

    public void setJmxProxy(MBeanProxy mBeanProxy) {}
}
