package de.mhus.lib.core.vault;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MValidator;
import de.mhus.lib.core.util.SecureString;

public class VaultSourceFromPlainJson extends MapMutableVaultSource {

    private boolean editable;
    private File file;
    private long fileModified;
    private boolean fileCanWrite;

    public VaultSourceFromPlainJson(File file, boolean editable, String name) throws IOException {
        this.file = file;
        this.name = name;
        this.editable = editable;
        if (file.exists())
            doLoad();
    }
    
    @Override
    public void doLoad() throws IOException {
        entries.clear();
        JsonNode json = MJson.load(file);
        for (JsonNode jEntry : json) {
            VaultEntry entry = new PlainEntry(jEntry);
            entries.put(entry.getId(), entry);
        }
        fileModified = file.lastModified();
        fileCanWrite = file.canWrite();
    }

    @Override
    public void doSave() throws IOException {
        ArrayNode json = MJson.createArrayNode();
        for (VaultEntry entry : entries.values()) {
            ObjectNode jEntry = MJson.createObjectNode();
            json.add(jEntry);
            jEntry.put("id", entry.getId().toString());
            jEntry.put("value", entry.getValue().value());
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

    private class PlainEntry implements VaultEntry {

        private UUID id;
        private SecureString value;
        private String type;
        private String desc;

        public PlainEntry(JsonNode node) {
            id = UUID.fromString(node.get("id").asText());
            value = new SecureString(node.get("value").asText());
            type = node.get("type").asText();
            desc = node.get("desc").asText();
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
        
    }
    
    @Override
    protected void doCheckSource() {
        if (file.lastModified() != fileModified)
            try {
                doLoad();
            } catch (IOException e) {
                log().e(file,e);
            }
    }
    
}
