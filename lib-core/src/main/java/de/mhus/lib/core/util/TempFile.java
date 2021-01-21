/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * The file will be deleted if the reference of the object is lost and the gc remove the object from
 * the vm.
 *
 * @author mikehummel
 */
public class TempFile extends File {

    private static final long serialVersionUID = 1L;

    public TempFile(File clone) {
        super(clone.getAbsolutePath());
        deleteOnExit();
    }

    public TempFile(File parent, String child) {
        super(parent, child);
        deleteOnExit();
    }

    public TempFile(String parent, String child) {
        super(parent, child);
        deleteOnExit();
    }

    public TempFile(String pathname) {
        super(pathname);
        deleteOnExit();
    }

    public TempFile(URI uri) {
        super(uri);
        deleteOnExit();
    }

    @Override
    protected void finalize() throws Throwable {
        delete(); // delete the tmp file if reference is lost
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        File tmpFile = File.createTempFile(prefix, suffix);
        return new TempFile(tmpFile);
    }
}
