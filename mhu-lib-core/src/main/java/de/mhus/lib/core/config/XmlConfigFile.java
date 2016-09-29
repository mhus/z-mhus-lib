package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.mhus.lib.core.MXml;
import de.mhus.lib.errors.MException;

/**
 * <p>XmlConfigFile class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class XmlConfigFile extends XmlConfig {

	private File file;

	/**
	 * <p>Constructor for XmlConfigFile.</p>
	 *
	 * @param file a {@link java.io.File} object.
	 * @throws java.io.FileNotFoundException if any.
	 * @throws java.lang.Exception if any.
	 */
	public XmlConfigFile(File file) throws FileNotFoundException, Exception  {
		super();
		if (file.exists())
			readConfig(new FileReader(file));
		this.file = file;
	}
	
	/**
	 * <p>Constructor for XmlConfigFile.</p>
	 *
	 * @param is a {@link java.io.InputStream} object.
	 * @throws java.io.FileNotFoundException if any.
	 * @throws java.lang.Exception if any.
	 */
	public XmlConfigFile(InputStream is) throws FileNotFoundException, Exception  {
		super();
			readConfig(new InputStreamReader(is));
		this.file = null;
	}
	
	/**
	 * <p>canSave.</p>
	 *
	 * @return a boolean.
	 */
	public boolean canSave() {
		if (file == null) return false;
		if (!file.exists()) {
			return file.getParentFile().canWrite();
		}
		return file.canWrite();
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
			MXml.trim(element);
			MXml.saveXml(element, os);
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
