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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.basics.RC;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ModelUtil;

public class DefRoot extends DefComponent implements Externalizable {

    private static final long serialVersionUID = 1L;
    public static final String ROOT = "root";
    private boolean build = false;

    public DefRoot() {
        super(null);
    }

    public DefRoot(IDefDefinition... definitions) {
        this(ROOT, definitions);
    }

    public DefRoot(String name, IDefDefinition... definitions) {
        super(name, definitions);
    }

    @Override
    public void inject(INode parent) throws MException {
        throw new MException(RC.CONFLICT, "can't link root into another container");
    }

    public synchronized DefRoot build() throws MException {
        if (build) return this;
        build = true;
        super.inject(null);
        return this;
    }

    public boolean isBuild() {
        return build;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        try {
            build();
        } catch (MException e) {
            throw new IOException(e);
        }
        ObjectNode json = ModelUtil.toJson(this);
        String content = MJson.toString(json);
        out.writeUTF(content);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String json = in.readUTF();
        DefRoot ext = ModelUtil.fromJson(json);
        this.properties = ext.properties;
        this.definitions = ext.definitions;
        this.name = ext.name;
    }
}
