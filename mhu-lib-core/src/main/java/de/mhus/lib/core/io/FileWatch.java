/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.io;

import java.io.File;
import java.util.TimerTask;

import de.mhus.lib.core.base.service.TimerIfc;

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

	public FileWatch(File fileToWatch, Listener listener) {
		this(fileToWatch, null, 30000, true, listener);
	}
	
	public FileWatch(File fileToWatch, TimerIfc timer, Listener listener) {
		this(fileToWatch, timer, 30000, true, listener);
	}
	/**
	 * Watch a file or directory (one level!) against changes. Check the modify date to recognize
	 * a change. It has two ways to work:
	 * 1. Manual check, every time you use the file, call the checkFile() method.
	 * 2. Use of a timer.
	 * 
	 * @param fileToWatch
	 * @param timer
	 * @param period
	 * @param startHook
	 * @param listener
	 */
	public FileWatch(File fileToWatch, TimerIfc timer, long period, boolean startHook, Listener listener) {
		file = fileToWatch;
		this.timer = timer;
		this.period = period;
		this.startHook = startHook;
		this.listener = listener;
	}
	
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
	
	public FileWatch doStop() {
		if (!started) return this;
		if (task != null)
			task.cancel();
		return this;
	}

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
