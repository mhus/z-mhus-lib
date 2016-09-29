package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.mhus.lib.errors.MException;

/**
 * <p>PropertiesConfigFile class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PropertiesConfigFile extends PropertiesConfig {

	private File file;

	/**
	 * <p>Constructor for PropertiesConfigFile.</p>
	 *
	 * @param file a {@link java.io.File} object.
	 * @throws java.io.IOException if any.
	 */
	public PropertiesConfigFile(File file) throws IOException {
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			properties.load(fis);
			fis.close();
		}
		this.file = file;
	}
	
	/**
	 * <p>Constructor for PropertiesConfigFile.</p>
	 *
	 * @param is a {@link java.io.InputStream} object.
	 * @throws java.io.IOException if any.
	 */
	public PropertiesConfigFile(InputStream is) throws IOException {
		properties.load(is);
		this.file = null;
	}
	
	/**
	 * <p>canSave.</p>
	 *
	 * @return a boolean.
	 */
	public boolean canSave() {
		return file != null && file.canWrite();
	}
	
	/**
	 * <p>save.</p>
	 *
	 * @throws de.mhus.lib.errors.MException if any.
	 */
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
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getClass() + ": " + (file == null ? "?" : file.getAbsolutePath());
	}

	/**
	 * <p>setName.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * <p>Getter for the field <code>file</code>.</p>
	 *
	 * @return a {@link java.io.File} object.
	 */
	public File getFile() {
		return file;
	}

}
