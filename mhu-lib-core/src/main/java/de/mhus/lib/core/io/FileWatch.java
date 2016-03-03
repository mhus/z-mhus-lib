package de.mhus.lib.core.io;

import java.io.File;
import java.util.TimerTask;

import de.mhus.lib.core.util.TimerIfc;

/**
 * <p>FileWatch class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FileWatch {

	private File file;
	private TimerIfc timer;
	private long period = 30 * 1000;
	private long modified = -2;
	private Listener listener;
	private boolean started = false;
	private TimerTask task;
	private long lastRun;
	private boolean startHook;

	/**
	 * <p>Constructor for FileWatch.</p>
	 *
	 * @param fileToWatch a {@link java.io.File} object.
	 * @param listener a {@link de.mhus.lib.core.io.FileWatch.Listener} object.
	 */
	public FileWatch(File fileToWatch, Listener listener) {
		this(fileToWatch, null, 30000, true, listener);
	}
	
	/**
	 * <p>Constructor for FileWatch.</p>
	 *
	 * @param fileToWatch a {@link java.io.File} object.
	 * @param timer a {@link de.mhus.lib.core.util.TimerIfc} object.
	 * @param listener a {@link de.mhus.lib.core.io.FileWatch.Listener} object.
	 */
	public FileWatch(File fileToWatch, TimerIfc timer, Listener listener) {
		this(fileToWatch, timer, 30000, true, listener);
	}
	/**
	 * Watch a file or directory (one level!) against changes. Check the modify date to recognize
	 * a change. It has two ways to work:
	 * 1. Manual check, every time you use the file, call the checkFile() method.
	 * 2. Use of a timer.
	 *
	 * @param fileToWatch a {@link java.io.File} object.
	 * @param timer a {@link de.mhus.lib.core.util.TimerIfc} object.
	 * @param period a long.
	 * @param startHook a boolean.
	 * @param listener a {@link de.mhus.lib.core.io.FileWatch.Listener} object.
	 */
	public FileWatch(File fileToWatch, TimerIfc timer, long period, boolean startHook, Listener listener) {
		file = fileToWatch;
		this.timer = timer;
		this.period = period;
		this.startHook = startHook;
		this.listener = listener;
	}
	
	/**
	 * <p>doStart.</p>
	 *
	 * @return a {@link de.mhus.lib.core.io.FileWatch} object.
	 */
	public FileWatch doStart() {
		if (started) return this;
		started = true; // do not need sync...
		if (startHook) checkFile(); // init
		if (timer != null) {
			this.task = new TimerTask() {

				@Override
				public void run() {
					checkFile();
				}
				
			};
			timer.schedule(task, period, period);
		}
		return this;
	}
	
	/**
	 * <p>doStop.</p>
	 *
	 * @return a {@link de.mhus.lib.core.io.FileWatch} object.
	 */
	public FileWatch doStop() {
		if (!started) return this;
		if (task != null)
			task.cancel();
		return this;
	}

	/**
	 * <p>checkFile.</p>
	 *
	 * @since 3.2.9
	 */
	public void checkFile() {
		if (System.currentTimeMillis() - lastRun < period) return;
		lastRun = System.currentTimeMillis();
		try {
			long modSum = 0;
			if (file.exists()) {
				if (file.isFile())
					modSum = file.lastModified();
				else
				if (file.isDirectory()) {
					for (File f : file.listFiles()) {
						if (f.isFile() && !f.isHidden()) {
							modSum+=f.lastModified();
						}
					}
				}
			} else {
				modSum = -1;
			}
			
			if (modified != -2 && listener != null && modified != modSum) {
				listener.onFileChanged(this);
			}
			modified = modSum;
			
		} catch (Throwable t) {
			if (listener != null)
				listener.onFileWatchError(this,t);
		}
	}
	
	/**
	 * <p>Getter for the field <code>file</code>.</p>
	 *
	 * @return a {@link java.io.File} object.
	 */
	public File getFile() {
		return file;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return file != null ? file.getAbsolutePath() : "?";
	}
	
	public static interface Listener {

		void onFileChanged(FileWatch fileWatch);

		void onFileWatchError(FileWatch fileWatch, Throwable t);
		
	}
}
