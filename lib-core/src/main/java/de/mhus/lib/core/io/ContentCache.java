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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.util.MObject;
import de.mhus.lib.errors.MException;

/**
 * TODO implement save write / read mechanism
 *
 * @author mikehummel
 */
public class ContentCache extends MObject {

    private File root;
    private boolean saveWrite = false; // enable thread / process save write

    public ContentCache(IConfig config) throws MException {
        if (config != null) {
            String rootString = config.getExtracted("root");
            saveWrite = config.getBoolean("save", saveWrite);
            if (rootString != null) {
                root = new File(rootString);
                if (!root.exists() || !root.isDirectory()) {
                    root = null;
                }
            }
        }

        if (root == null) {
            log().w("cache disabled");
        }
    }

    public boolean isCached(String id) {
        if (root == null || id == null) return false;
        File dir = getDir(id);
        if (!dir.exists()) return false;
        File file = getFile(dir, id);
        return file.exists();
    }

    public InputStream cacheAndReturn(String id, InputStream is) throws IOException {
        if (root == null || id == null) return is;
        doCache(id, is);
        return getCached(id);
    }

    public void doCache(String id, InputStream is) throws IOException {
        if (root == null || id == null) return;
        openLock();
        log().d("cache", id);
        File dir = getDir(id);
        if (!dir.exists()) dir.mkdirs();
        File file = getFile(dir, id);
        OutputStream os = new FileOutputStream(file);
        MFile.copyFile(is, os);
        is.close();
        os.close();
        closeLock();
    }

    private void closeLock() {
        if (!saveWrite) return;
    }

    private void openLock() {
        if (!saveWrite) return;
    }

    public InputStream getCached(String id) throws FileNotFoundException {
        if (root == null || id == null) return null;
        log().d("load cached", id);
        File dir = getDir(id);
        if (!dir.exists()) dir.mkdirs();
        File file = getFile(dir, id);
        if (!file.exists()) return null;
        return new FileInputStream(file);
    }

    private File getFile(File dir, String id) {
        if (id.length() < 3) return new File(dir, id + ".bin");
        return new File(dir, id.substring(id.length() - 3) + ".bin");
    }

    private File getDir(String id) {
        if (id.length() < 3) return root;
        StringBuilder x = new StringBuilder();
        for (int i = 0; i < id.length() - 3; i++) {
            if (i % 3 == 0 && i != 0) x.append("/");
            x.append(id.charAt(i));
        }
        return new File(root, x.toString());
    }
}
