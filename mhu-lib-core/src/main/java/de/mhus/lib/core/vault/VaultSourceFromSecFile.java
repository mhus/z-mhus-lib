/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.vault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.crypt.MCrypt;
import de.mhus.lib.core.util.SecureString;
import de.mhus.lib.errors.MException;

public class VaultSourceFromSecFile extends MapMutableVaultSource {

	private SecureString passphrase;
	private File file;

	public VaultSourceFromSecFile(File file, String passphrase, String name) throws IOException {
		this(file,passphrase);
		this.name = name;
	}
	public VaultSourceFromSecFile(File file, String passphrase) throws IOException {
		this.passphrase = new SecureString(passphrase);
		this.file = file;
		if (file.exists())
			doLoad();
	}
	
	@Override
	public void doLoad() throws IOException {
		FileInputStream parent = new FileInputStream(file);
		InputStream is = MCrypt.createCipherInputStream(parent, passphrase.value());
		ObjectInputStream ois = new ObjectInputStream(is);
		name = ois.readUTF();
		int size = ois.readInt();
		entries.clear();
		for (int i = 0; i < size; i++) {
			VaultEntry entry = new FileEntry(ois);
			try {
				addEntry(entry);
			} catch (MException e) {
				log().d(entry,e);
			}
		}
		parent.close();
	}

	@Override
	public void doSave() throws IOException {
		FileOutputStream parent = new FileOutputStream(file);
		OutputStream os = MCrypt.createCipherOutputStream(parent, passphrase.value());
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeInt(1); // version
		oos.writeUTF(name);
		oos.writeInt(entries.size());
		for (VaultEntry entry : entries.values()) {
			oos.writeUTF(entry.getId().toString());
			oos.writeUTF(entry.getType());
			oos.writeUTF(entry.getDescription());
			oos.writeObject(entry.getValue());
		}
		oos.flush();
		parent.close();
	}
	
	private class FileEntry extends DefaultEntry {

		public FileEntry(ObjectInputStream ois) throws IOException {
			int v = ois.readInt();
			if (v == 1) {
				id = UUID.fromString(ois.readUTF());
				type = ois.readUTF();
				description = ois.readUTF();
				try {
					value = (SecureString)ois.readObject();
				} catch (ClassNotFoundException e) {
					throw new IOException(e);
				}
			}
		}
		
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, name, entries.size(), file);
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
    protected void doCheckSource() {
    }


}
