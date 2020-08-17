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
package de.mhus.lib.core.util;

import java.util.Iterator;
import java.util.LinkedList;

public class InjectorList implements Injector, Iterable<Injector> {

    protected LinkedList<Injector> list = new LinkedList<Injector>();

    public int size() {
        return list.size();
    }

    public boolean add(Injector e) {
        return list.add(e);
    }

    @Override
    public Iterator<Injector> iterator() {
        return list.iterator();
    }

    public void clear() {
        list.clear();
    }

    public Injector get(int index) {
        return list.get(index);
    }

    @Override
    public void doInject(Object obj) throws Exception {
        for (Injector i : list) i.doInject(obj);
    }
}
