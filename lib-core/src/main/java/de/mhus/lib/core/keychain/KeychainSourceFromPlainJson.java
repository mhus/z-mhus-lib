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
package de.mhus.lib.core.keychain;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.util.SecureString;

public class KeychainSourceFromPlainJson extends MapMutableVaultSource {

    private boolean editable;
    private File file;
    private long fileModified;
    private boolean fileCanWrite;

    public KeychainSourceFromPlainJson(File file, boolean editable, String name)
            throws IOException {
        this.file = file;
        this.name = name;
        this.editable = editable;
        if (file.exists()) doLoad();
    }

    @Override
    public void doLoad() throws IOException {
        entries.clear();
        JsonNode json = MJson.load(file);
        for (JsonNode jEntry : json) {
            KeyEntry entry = new PlainEntry(jEntry);
            entries.put(entry.getId(), entry);
        }
        fileModified = file.lastModified();
        fileCanWrite = file.canWrite();
    }

    @Override
    public void doSave() throws IOException {
        ArrayNode json = MJson.createArrayNode();
        for (KeyEntry entry : entries.values()) {
            ObjectNode jEntry = MJson.createObjectNode();
            json.add(jEntry);
            jEntry.put("id", entry.getId().toString());
            jEntry.put("value", entry.getValue().value());
            jEntry.put("name", entry.getName());
            jEntry.put("type", entry.getType());
            jEntry.put("desc", entry.getDescription());
        }
        MJson.save(json, file);
        fileModified = file.lastModified();
    }

    @Override
    public boolean isMemoryBased() {
        return true;
    }

    @Override
    public MutableVaultSource getEditable() {
        if (!editable || !fileCanWrite) return null;
        return this;
    }

    @Override
    public String toString() {
        return MSystem.toString(this, name, entries.size(), file);
    }

    private class PlainEntry implements KeyEntry {

        private UUID id;
        private SecureString value;
        private String type;
        private String name;
        private String desc;

        public PlainEntry(JsonNode node) {
            id = UUID.fromString(node.get("id").asText());
            value = new SecureString(node.get("value").asText());
            name = M.get(node, "name", null);
            type = M.get(node, "type", null);
            desc = M.get(node, "desc", null);
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getDescription() {
            return desc;
        }

        @Override
        public SecureString getValue() {
            return value;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    protected void doCheckSource() {
        if (file.lastModified() != fileModified)
            try {
                doLoad();
            } catch (IOException e) {
                log().e(file, e);
            }
    }
}
