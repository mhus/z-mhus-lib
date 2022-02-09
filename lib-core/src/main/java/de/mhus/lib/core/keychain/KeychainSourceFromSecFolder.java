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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.UUID;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MValidator;
import de.mhus.lib.core.crypt.MCrypt;
import de.mhus.lib.core.util.SecureString;

public class KeychainSourceFromSecFolder extends MapMutableVaultSource {

    private static final int VERSION_LAST = 2;
    private SecureString passphrase;
    private File folder;
    private int version;

    public KeychainSourceFromSecFolder(File folder, String passphrase, String name)
            throws IOException {
        this(folder, passphrase);
        this.name = name;
    }

    public KeychainSourceFromSecFolder(File folder, String passphrase) throws IOException {
        this.passphrase = new SecureString(passphrase);
        this.folder = folder;
        if (folder.exists()) doLoad();
        else folder.mkdirs();
    }

    @Override
    public void doLoad() throws IOException {
        version = 0;
        {
            File file = new File(folder, "info.txt");
            if (file.exists()) name = MFile.readFile(file).trim();
        }
        {
            File file = new File(folder, "version.txt");
            if (file.exists()) version = MCast.toint(MFile.readFile(file).trim(), 0);
        }
        entries.clear();
        for (File file : folder.listFiles()) {
            if (!file.getName().startsWith(".")
                    && file.isFile()
                    && MValidator.isUUID(file.getName())) {
                loadEntry(file);
            }
        }
    }

    protected void loadEntry(File file) throws IOException {
        FileInputStream parent = new FileInputStream(file);
        InputStream is = MCrypt.createCipherInputStream(parent, passphrase.value());
        ObjectInputStream ois = new ObjectInputStream(is);
        try {
            KeyEntry entry = new FileEntry(ois);
            addEntry(entry);
        } catch (Exception e) {
            log().w("load entry {1} failed", file, e);
        }
        parent.close();
    }

    @Override
    public void doSave() throws IOException {
        version = VERSION_LAST;
        {
            File file = new File(folder, "info.txt");
            MFile.writeFile(file, name);
        }
        {
            File file = new File(folder, "version.txt");
            MFile.writeFile(file, "" + version);
        }
        HashSet<String> ids = new HashSet<>();
        for (KeyEntry entry : entries.values()) {
            ids.add(entry.getId().toString());
            saveEntry(entry);
        }
        for (File file : folder.listFiles()) {
            if (!file.getName().startsWith(".")
                    && file.isFile()
                    && MValidator.isUUID(file.getName())) {
                if (!ids.contains(file.getName())) file.delete();
            }
        }
    }

    protected void saveEntry(KeyEntry entry) throws IOException {
        File file = new File(folder, entry.getId().toString());
        FileOutputStream parent = new FileOutputStream(file);
        OutputStream os = MCrypt.createCipherOutputStream(parent, passphrase.value());
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeInt(version);
        oos.writeUTF(entry.getId().toString());
        oos.writeUTF(entry.getType());
        oos.writeUTF(entry.getName());
        oos.writeUTF(entry.getDescription());
        oos.writeObject(entry.getValue());
        oos.flush();
        parent.close();
    }

    private class FileEntry extends DefaultEntry {

        public FileEntry(ObjectInputStream ois) throws IOException {

            int v = ois.readInt();
            if (v == 1 || v == 2) {
                id = UUID.fromString(ois.readUTF());
                type = ois.readUTF();
                if (v == 2) name = ois.readUTF();
                description = ois.readUTF();
                try {
                    value = (SecureString) ois.readObject();
                } catch (ClassNotFoundException e) {
                    throw new IOException(e);
                }
            }
        }
    }

    @Override
    public String toString() {
        return MSystem.toString(this, name, entries.size(), folder);
    }

    @Override
    public boolean isMemoryBased() {
        return true;
    }

    @Override
    public MutableVaultSource getEditable() {
        return this;
    }

    @Override
    protected void doCheckSource() {}
}
