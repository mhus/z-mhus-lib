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

import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

public class JmxWeakMap extends JmxObject implements JmxWeakMapMBean {

    private WeakHashMap<?, ?> map;

    public JmxWeakMap(Object owner, String name, WeakHashMap<?, ?> map) {
        if (owner != null) setJmxPackage(owner.getClass().getCanonicalName());
        setJmxName(name);
        this.map = map;
    }

    @Override
    public int getSize() {
        return map.size();
    }

    @Override
    public String[] getEntries() {
        LinkedList<String> out = new LinkedList<String>();
        for (Map.Entry<?, ?> item : map.entrySet()) {
            out.add(item.getKey() + "=" + item.getValue());
        }
        return out.toArray(new String[out.size()]);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }
}
