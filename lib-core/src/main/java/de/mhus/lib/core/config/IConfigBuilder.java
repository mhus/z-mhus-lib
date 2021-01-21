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
package de.mhus.lib.core.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.errors.MException;

public abstract class IConfigBuilder extends MLog {

    public abstract IConfig read(InputStream is) throws MException;

    public abstract void write(IConfig config, OutputStream os) throws MException;

    public IConfig readFromFile(File file) throws MException {
        try (FileInputStream is = new FileInputStream(file)) {
            return read(is);
        } catch (IOException e) {
            throw new MException(file, e);
        }
    }

    public IConfig readFromString(String content) throws MException {
        return read(new ByteArrayInputStream(MString.toBytes(content)));
    }

    public void writeToFile(IConfig config, File file) throws MException {
        try (FileOutputStream os = new FileOutputStream(file)) {
            write(config, os);
        } catch (IOException e) {
            throw new MException(file, e);
        }
    }

    public String writeToString(IConfig config) throws MException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        write(config, os);
        return MString.byteToString(os.toByteArray());
    }
}
