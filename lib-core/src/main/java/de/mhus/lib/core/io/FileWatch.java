/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.io;

import java.io.File;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;

public class FileWatch extends MHousekeeperTask {

    private File file;
    private long period = 30 * 1000;
    private long modified = -2;
    private Listener listener;
    private boolean started = false;
    private long lastRun;
    private boolean startHook;
    private boolean registered = false;

    public FileWatch(File fileToWatch, Listener listener) {
        this(fileToWatch, 30000, true, listener);
    }

    /**
     * Watch a file or directory (one level!) against changes. Check the modify date to recognize a
     * change. It has two ways to work: 1. Manual check, every time you use the file, call the
     * checkFile() method. 2. Use of a timer.
     *
     * @param fileToWatch
     * @param period
     * @param startHook
     * @param listener
     */
    public FileWatch(File fileToWatch, long period, boolean startHook, Listener listener) {
        file = fileToWatch;
        this.period = period;
        this.startHook = startHook;
        this.listener = listener;
        setName(file.getName());
    }

    public FileWatch doStart() {
        if (started) return this;
        started = true; // do not need sync...

        if (!file.exists()) log().w("file not found", file);

        if (startHook) checkFile(); // init

        if (!registered) {
            MHousekeeper housekeeper = M.l(MHousekeeper.class);
            housekeeper.register(this, period);
            registered = true;
        }
        return this;
    }

    public FileWatch doStop() {
        if (!started) return this;
        return this;
    }

    public void checkFile() {
        if (System.currentTimeMillis() - lastRun < period) return;
        lastRun = System.currentTimeMillis();
        try {
            long modSum = 0;
            if (file.exists()) {
                if (file.isFile()) modSum = file.lastModified();
                else if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        if (f.isFile() && !f.isHidden()) {
                            modSum += f.lastModified();
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
            if (listener != null) listener.onFileWatchError(this, t);
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

    @Override
    protected void doit() throws Exception {
        checkFile();
    }
}
