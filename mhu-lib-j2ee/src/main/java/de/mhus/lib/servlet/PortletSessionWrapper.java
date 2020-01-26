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
package de.mhus.lib.servlet;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletSession;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.util.MapEntry;
import de.mhus.lib.errors.NotSupportedException;

public class PortletSessionWrapper extends AbstractProperties {

    private static final long serialVersionUID = 1L;
    private PortletSession instance;

    public PortletSessionWrapper(PortletSession session) {
        instance = session;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new NotSupportedException();
    }

    @Override
    public Collection<Object> values() {
        LinkedList<Object> out = new LinkedList<>();
        for (Enumeration<String> en = instance.getAttributeNames(); en.hasMoreElements(); ) {
            out.add(instance.getAttribute(en.nextElement()));
        }
        return out;
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        HashSet<Map.Entry<String, Object>> out = new HashSet<>();
        for (Enumeration<String> en = instance.getAttributeNames(); en.hasMoreElements(); ) {
            String name = en.nextElement();
            out.add(new MapEntry<String, Object>(name, instance.getAttribute(name)));
        }
        return out;
    }

    @Override
    public Object getProperty(String name) {
        return instance.getAttribute(name);
    }

    @Override
    public boolean isProperty(String name) {
        return instance.getAttribute(name) != null;
    }

    @Override
    public void removeProperty(String key) {
        instance.removeAttribute(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        instance.setAttribute(key, value);
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public Set<String> keys() {
        HashSet<String> out = new HashSet<>();
        for (Enumeration<String> en = instance.getAttributeNames(); en.hasMoreElements(); ) {
            String name = en.nextElement();
            out.add(name);
        }
        return out;
    }

    @Override
    public int size() {
        int cnt = 0;
        for (Enumeration<String> en = instance.getAttributeNames(); en.hasMoreElements(); ) cnt++;
        return cnt;
    }

    @Override
    public void clear() {
        for (String key : keys()) remove(key);
    }
}
