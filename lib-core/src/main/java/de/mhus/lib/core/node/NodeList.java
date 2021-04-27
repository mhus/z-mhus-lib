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
package de.mhus.lib.core.node;

import java.util.Collection;
import java.util.LinkedList;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.MRuntimeException;

public class NodeList extends LinkedList<INode> {

    private static final long serialVersionUID = 1L;
    private String name;
    private MNode parent;

    public NodeList(String name, MNode parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean addAll(int index, Collection<? extends INode> c) {
        c.forEach(
                i -> {
                    ((MNode) i).name = name;
                    ((MNode) i).parent = parent;
                });
        return super.addAll(index, c);
    }

    @Override
    public boolean add(INode e) {
        ((MNode) e).name = name;
        ((MNode) e).parent = parent;
        return super.add(e);
    }

    public boolean add(IProperties e) {
        MNode node = new MNode();
        node.parent = parent;
        node.putAll(e);
        return super.add(node);
    }
    
    public INode add(NodeSerializable object) {
        INode cfg = createObject();
        try {
            object.writeSerializabledNode(cfg);
        } catch (Exception e) {
            throw new MRuntimeException(e);
        }
        return cfg;
    }

    @Override
    public void addFirst(INode e) {
        ((MNode) e).name = name;
        ((MNode) e).parent = parent;
        super.addFirst(e);
    }

    @Override
    public void addLast(INode e) {
        ((MNode) e).name = name;
        ((MNode) e).parent = parent;
        super.addLast(e);
    }

    @Override
    public INode set(int index, INode e) {
        if (e instanceof MNode) {
            ((MNode) e).name = name;
            ((MNode) e).parent = parent;
        }
        return super.set(index, e);
    }

    public INode createObject() {
        MNode ret = new MNode(name, parent);
        super.add(ret);
        return ret;
    }
}
