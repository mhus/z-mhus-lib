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
package de.mhus.lib.core.config;

import java.io.File;
import java.net.URL;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.errors.MException;

@DefaultImplementation(DefaultConfigFactory.class)
public interface IConfigFactory {

    IConfig read(Class<?> owner, String fileName) throws MException;

    IConfig read(File file) throws MException;

    IConfig read(URL url) throws MException;

    IConfig create();

    void write(IConfig config, File file) throws MException;

    IConfigBuilder getBuilder(String ext);

    /**
     * This will search a file with different file extensions
     *
     * @param path Path to file without file extension
     * @return The config object or null
     * @throws MException
     */
    public default IConfig find(String path) throws MException {
        File f = new File(path);
        return find(f.getParentFile(), f.getName());
    }

    /**
     * This will search a file with different file extensions
     *
     * @param parent
     * @param name Name of file without file extension
     * @return The config object or null
     * @throws MException
     */
    public default IConfig find(File parent, String name) throws MException {
        {
            File f = new File(parent, name + ".xml");
            if (f.exists() && f.isFile()) read(f);
        }
        {
            File f = new File(parent, name + ".json");
            if (f.exists() && f.isFile()) read(f);
        }
        {
            File f = new File(parent, name + ".yaml");
            if (f.exists() && f.isFile()) read(f);
        }
        {
            File f = new File(parent, name + ".yml");
            if (f.exists() && f.isFile()) read(f);
        }
        {
            File f = new File(parent, name + ".properties");
            if (f.exists() && f.isFile()) read(f);
        }
        {
            File f = new File(parent, name);
            if (f.exists() && f.isDirectory()) read(f);
        }
        return null;
    }
}
