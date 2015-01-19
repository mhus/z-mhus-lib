package de.mhus.lib.core.io;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class FileWatch extends TimerTask {

	private File file;
	private Timer timer;
	private long period = 30 * 1000;
	private long size = -2;
	private Listener listener;

	public FileWatch(File fileToWatch, Timer timer, Listener listener) {
		this(fileToWatch, timer, 30000, false, listener);
	}
	
	public FileWatch(File fileToWatch, Timer timer, long period, boolean startHook, Listener listener) {
		file = fileToWatch;
		this.timer = timer;
		this.period = period;
		if (startHook) size = -3;
		this.listener = listener;
	}
	
	public FileWatch doStart() {
		timer.schedule(this, 0, period);
		return this;
	}
	
	public FileWatch doStop() {
		cancel();
		return this;
	}

	@Override
	public void run() {
		try {
			long newSize = 0;
			if (file.exists()) {
				if (file.isFile())
					newSize = file.length();
				else
				if (file.isDirectory()) {
					for (File f : file.listFiles()) {
						if (f.isFile() && !f.isHidden()) {
							newSize+=f.length();
						}
					}
				}
			} else {
				newSize = -1;
			}
			
			if (size != -2 && listener != null) {
				listener.onFileChanged(this);
			}
			size = newSize;
			
		} catch (Throwable t) {
			if (listener != null)
				listener.onFileWatchError(this,t);
		}
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	public String toString() {
		return file != null ? file.getAbsolutePath() : "?";
	}
	
	public static interface Listener {

		void onFileChanged(FileWatch fileWatch);

		void onFileWatchError(FileWatch fileWatch, Throwable t);
		
	}
}
