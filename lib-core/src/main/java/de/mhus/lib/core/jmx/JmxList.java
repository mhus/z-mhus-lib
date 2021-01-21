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
import java.util.List;

public class JmxList extends JmxObject implements JmxListMBean {

    private List<?> list;

    public JmxList(Object owner, String name, List<?> list) {
        if (owner != null) setJmxPackage(owner.getClass().getCanonicalName());
        setJmxName(name);
        this.list = list;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public String[] getEntries() {
        LinkedList<String> out = new LinkedList<String>();
        for (Object item : list) {
            out.add(String.valueOf(item));
        }
        return out.toArray(new String[out.size()]);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void remove(int key) {
        list.remove(key);
    }
}
