package de.mhus.lib.core.vault;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MValidator;
import de.mhus.lib.core.util.SecureString;

public class VaultSourceFromPlainProperties extends MapMutableVaultSource {

    private boolean editable;
    private File file;
    private long fileModified;
    private boolean fileCanWrite;

    public VaultSourceFromPlainProperties(File file, boolean editable, String name) throws IOException {
        this.file = file;
        this.name = name;
        this.editable = editable;
        if (file.exists())
            doLoad();
    }
    
    @Override
    public void doLoad() throws IOException {
        entries.clear();
        MProperties prop = MProperties.load(file);
        for (String key : prop.keys()) {
            if (MValidator.isUUID(key)) {
                VaultEntry entry = new PlainEntry( prop, name);
                entries.put(UUID.fromString(name), entry);
            }
        }
        fileModified = file.lastModified();
        fileCanWrite = file.canWrite();
    }

    @Override
    public void doSave() throws IOException {
        MProperties out = new MProperties();
        for (VaultEntry entry : entries.values()) {
            out.setString(entry.getId().toString(), entry.getValue().value());
            out.setString(entry.getId() + ".type", entry.getType());
            out.setString(entry.getId() + ".desc", entry.getDescription());
        }
        out.save(file);
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

        public PlainEntry(MProperties prop, String name) {
            id = UUID.fromString(name);
            value = new SecureString(prop.getString(name, null));
            type = prop.getString(name + ".type", "");
            desc = prop.getString(name + ".desc", "");
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
