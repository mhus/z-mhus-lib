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

public class FileVaultSource extends MutableVaultSource {

	private SecureString passphrase;
	private File file;

	public FileVaultSource(File file, String passphrase, String name) throws IOException {
		this(file,passphrase);
		this.name = name;
	}
	public FileVaultSource(File file, String passphrase) throws IOException {
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
			addEntry(entry);
		}
		parent.close();
	}

	@Override
	public void doSave() throws IOException {
		FileOutputStream parent = new FileOutputStream(file);
		OutputStream os = MCrypt.createCipherOutputStream(parent, passphrase.value());
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeUTF(name);
		oos.writeInt(entries.size());
		for (VaultEntry entry : entries.values()) {
			oos.writeUTF(entry.getId().toString());
			oos.writeUTF(entry.getType());
			oos.writeUTF(entry.getDescription());
			oos.writeUTF(entry.getValue());
		}
		oos.flush();
		parent.close();
	}
	
	private class FileEntry extends DefaultEntry {

		public FileEntry(ObjectInputStream ois) throws IOException {
			id = UUID.fromString(ois.readUTF());
			type = ois.readUTF();
			description = ois.readUTF();
			value = new SecureString(ois.readUTF());
		}
		
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, name, entries.size(), file);
	}


}
