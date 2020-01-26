/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.lang.MObject;

public class Unzip extends MObject {

    public void unzip(File src, File dst, FileFilter filter) throws ZipException, IOException {
        Enumeration<?> entries;
        ZipFile zipFile;
        zipFile = new ZipFile(src);

        entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();

            if (entry.isDirectory()) {
                // Assume directories are stored parents first then children.
                // System.err.println("Extracting directory: " + entry.getName());
                log().t("Unzip directory: " + entry.getName());
                // This is not robust, just for demonstration purposes.
                (new File(dst, entry.getName())).mkdir();
                continue;
            }

            File dstFile = new File(dst, entry.getName());
            if (filter == null || !filter.accept(dstFile)) {
                log().t("Unzip file: " + entry.getName());
                File parent = dstFile.getParentFile();
                if (!parent.exists()) {
                    log().t("  Create parent");
                    parent.mkdirs();
                }
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(dstFile));
                MFile.copyFile(zipFile.getInputStream(entry), os);
                os.flush();
                os.close();
            }
        }

        zipFile.close();
    }
}
