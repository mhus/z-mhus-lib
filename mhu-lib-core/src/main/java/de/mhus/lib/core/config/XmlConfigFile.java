package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.mhus.lib.core.MXml;
import de.mhus.lib.errors.MException;

public class XmlConfigFile extends XmlConfig {

	private static final long serialVersionUID = 1L;
	private File file;

	public XmlConfigFile(File file) throws FileNotFoundException, Exception  {
		super();
		if (file.exists())
			readConfig(new FileReader(file));
		this.file = file;
	}
	
	public XmlConfigFile(InputStream is) throws FileNotFoundException, Exception  {
		super();
			readConfig(new InputStreamReader(is));
		this.file = null;
	}
	
	public boolean canSave() {
		if (file == null) return false;
		if (!file.exists()) {
			return file.getParentFile().canWrite();
		}
		return file.canWrite();
	}
	
	public void save() throws MException {
		try {
			if (!canSave()) return;
			log().t("save config",this);
			FileOutputStream os = new FileOutputStream(file);
			MXml.trim(element);
			MXml.saveXml(element, os);
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
