package de.mhus.lib.core.cfg;

import java.io.File;
import java.io.IOException;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.base.service.TimerIfc;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.PropertiesConfigFile;
import de.mhus.lib.core.io.FileWatch;

public class PropertiesCfgFileWatch extends MLog implements CfgProvider {


	private FileWatch fileWatch;
	private File file;
	private IConfig config;
	private String name;
	
	public PropertiesCfgFileWatch() {
	}
	
	public PropertiesCfgFileWatch(File file) {
		setFile(file);
	}
	
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

	public void doStop() {
		if (fileWatch != null) {
			fileWatch.doStop();
			fileWatch = null;
		}
	}
	
	
	@Override
	public IConfig getConfig() {
		return config;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
