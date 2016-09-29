package de.mhus.lib.core.cfg;

import java.io.File;
import java.io.IOException;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.PropertiesConfigFile;
import de.mhus.lib.core.io.FileWatch;
import de.mhus.lib.core.util.TimerIfc;

/**
 * <p>PropertiesCfgFileWatch class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class PropertiesCfgFileWatch extends MLog implements CfgProvider {


	private FileWatch fileWatch;
	private File file;
	private IConfig config;
	private String name;
	
	/**
	 * <p>Constructor for PropertiesCfgFileWatch.</p>
	 */
	public PropertiesCfgFileWatch() {
	}
	
	/**
	 * <p>Constructor for PropertiesCfgFileWatch.</p>
	 *
	 * @param file a {@link java.io.File} object.
	 */
	public PropertiesCfgFileWatch(File file) {
		setFile(file);
	}
	
	/** {@inheritDoc} */
	public void doStart(final String name) {
		this.name = name;
		load();
		MSingleton.getCfgUpdater().doUpdate(name);
		
		TimerIfc timer = MSingleton.get().getBaseControl().getCurrentBase().lookup(TimerIfc.class);
		fileWatch = new FileWatch(file, timer, new FileWatch.Listener() {

			@Override
			public void onFileChanged(FileWatch fileWatch) {
				log().d("update cfg properties file",file);
				load();
				MSingleton.getCfgUpdater().doUpdate(name);
			}

			@Override
			public void onFileWatchError(FileWatch fileWatch, Throwable t) {
				log().d(file,t);
			}
			
		});
		fileWatch.doStart();
	}
	
	private void load() {
		try {
			config = new PropertiesConfigFile(file);
		} catch (IOException e) {
			log().d(file,e);
		}
	}

	/**
	 * <p>doStop.</p>
	 */
	public void doStop() {
		if (fileWatch != null) {
			fileWatch.doStop();
			fileWatch = null;
		}
	}
	
	
	/** {@inheritDoc} */
	@Override
	public IConfig getConfig() {
		return config;
	}

	/**
	 * <p>Getter for the field <code>file</code>.</p>
	 *
	 * @return a {@link java.io.File} object.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * <p>Setter for the field <code>file</code>.</p>
	 *
	 * @param file a {@link java.io.File} object.
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
