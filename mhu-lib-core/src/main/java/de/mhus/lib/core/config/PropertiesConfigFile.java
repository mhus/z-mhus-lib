package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.mhus.lib.errors.MException;

public class PropertiesConfigFile extends PropertiesConfig {

	private File file;

	public PropertiesConfigFile(File file) throws IOException {
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			properties.load(fis);
			fis.close();
		}
		this.file = file;
	}
	
	public PropertiesConfigFile(InputStream is) throws IOException {
		properties.load(is);
		this.file = null;
	}
	
	public boolean canSave() {
		return file != null && file.canWrite();
	}
	
	public void save() throws MException {
		try {
			if (!canSave()) return;
			log().t("save config",this);
			FileOutputStream os = new FileOutputStream(file);
			properties.store(os, "");
			os.close();
		} catch (Exception e) {
			throw new MException(e);
		}
	}
	
	@Override
	public String toString() {
		return getClass() + ": " + (file == null ? "?" : file.getAbsolutePath());
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public File getFile() {
		return file;
	}

}
