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
package de.mhus.lib.core.config;

import java.util.Collection;
import java.util.LinkedList;

public class ConfigList extends LinkedList<IConfig> {

    private static final long serialVersionUID = 1L;
    private String name;
    private MConfig parent;

    public ConfigList(String name, MConfig parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean addAll(int index, Collection<? extends IConfig> c) {
        c.forEach(
                i -> {
                    ((MConfig) i).name = name;
                    ((MConfig) i).parent = parent;
                });
        return super.addAll(index, c);
    }

    @Override
    public boolean add(IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        return super.add(e);
    }

    @Override
    public void addFirst(IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        super.addFirst(e);
    }

    @Override
    public void addLast(IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        super.addLast(e);
    }

    @Override
    public IConfig set(int index, IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        return super.set(index, e);
    }

    public IConfig createObject() {
        MConfig ret = new MConfig(name, parent);
        super.add(ret);
        return ret;
    }
}
