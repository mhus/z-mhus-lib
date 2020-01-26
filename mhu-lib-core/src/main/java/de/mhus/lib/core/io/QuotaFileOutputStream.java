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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QuotaFileOutputStream extends FileOutputStream {

    private File file;
    private long quota;

    public QuotaFileOutputStream(File file, Long quota) throws FileNotFoundException {
        super(file);
        this.file = file;
        this.quota = quota;
    }

    @Override
    public void write(int b) throws IOException {
        if (file.length() + 1 > quota) throw new IOException("maximum file size reached " + quota);
        super.write(b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        if (file.length() + b.length > quota)
            throw new IOException("maximum file size reached " + quota);
        super.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        if (file.length() + len > quota)
            throw new IOException("maximum file size reached " + quota);
        super.write(b, off, len);
    }
}
