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
package de.mhus.lib.core.util;

import java.io.Serializable;

import de.mhus.lib.core.node.NodeSerializable;
import de.mhus.lib.core.node.INode;

public class TableColumn implements Serializable, NodeSerializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String note;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void readSerializabledNode(INode cfg) throws Exception {
        name = cfg.getString("name", null);
        type =cfg.getString("type", null);
        note = cfg.getString("note", null);
    }

    @Override
    public void writeSerializabledNode(INode cfg) throws Exception {
        cfg.setString("name", name);
        cfg.setString("type", type);
        cfg.setString("note", note);
    }

    @Override
    public String toString() {
        return name + ":" + type;
    }
}
