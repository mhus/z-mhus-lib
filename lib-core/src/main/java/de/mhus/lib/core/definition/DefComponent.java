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
package de.mhus.lib.core.definition;

import java.util.LinkedList;
import java.util.Properties;

import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.MNode;
import de.mhus.lib.errors.MException;

public class DefComponent extends MNode implements IDefDefinition {

    private static final long serialVersionUID = 1L;
    private LinkedList<IDefDefinition> definitions = new LinkedList<IDefDefinition>();

    public DefComponent(String tag, IDefDefinition... definitions) {
        super(tag);
        addDefinition(definitions);
    }

    public DefComponent addAttribute(String name, Object value) {
        return addDefinition(new DefAttribute(name, value));
    }

    public DefComponent addDefinition(IDefDefinition... def) {
        if (def == null) return this;
        for (IDefDefinition d : def) if (d != null) definitions.add(d);
        return this;
    }

    public LinkedList<IDefDefinition> definitions() {
        return definitions;
    }

    @Override
    public void inject(DefComponent parent) throws MException {
        if (parent != null) {
            MNode obj = new MNode(getName());
            obj.putAll(this);
            parent.getArrayOrCreate(getName()).add(obj);
            //parent.setObject(tag, this);
        }
        for (IDefDefinition d : definitions) {
            d.inject(this);
        }
    }

    public void fillNls(Properties p) throws MException {

        String nls = getString("nls", null);
        if (nls == null) nls = getString("name", null);
        if (nls != null && isProperty("title")) {
            p.setProperty(nls + "_title", getString("title", null));
        }
        if (nls != null && isProperty("description")) {
            p.setProperty(nls + "_description", getString("description", null));
        }

        fill(this, p);
    }

    private void fill(INode config, Properties p) throws MException {
        for (INode c : config.getObjects()) {
            if (c instanceof DefComponent) ((DefComponent) c).fillNls(p);
            else fill(c, p);
        }
    }
}
