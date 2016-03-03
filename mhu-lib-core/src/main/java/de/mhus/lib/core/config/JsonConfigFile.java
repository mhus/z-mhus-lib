package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.mhus.lib.core.MFile;
import de.mhus.lib.errors.MException;

/**
 * <p>JsonConfigFile class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JsonConfigFile extends JsonConfig {

	private File file;

	/**
	 * <p>Constructor for JsonConfigFile.</p>
	 *
	 * @param file a {@link java.io.File} object.
	 * @throws java.lang.Exception if any.
	 */
	public JsonConfigFile(File file) throws Exception {
		super(MFile.readFile(file));
		this.file = file;
	}
	
	/**
	 * <p>Constructor for JsonConfigFile.</p>
	 *
	 * @param is a {@link java.io.InputStream} object.
	 * @throws java.lang.Exception if any.
	 */
	public JsonConfigFile(InputStream is) throws Exception {
		super(MFile.readFile(new InputStreamReader(is))); // read utf
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
			write(os);
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
