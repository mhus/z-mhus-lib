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
package de.mhus.lib.core.cfg;

import java.io.File;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.IConfigFactory;
import de.mhus.lib.core.io.FileWatch;
import de.mhus.lib.errors.MException;

public class PropertiesCfgFileWatch extends CfgProvider {

    private FileWatch fileWatch;
    private File file;
    private IConfig config;

    public PropertiesCfgFileWatch(String name, File file) {
        super(name);
        setFile(file);
    }

    @Override
    public void doStart() {
        load();
        MApi.getCfgUpdater().doUpdate(getName());

        fileWatch =
                new FileWatch(
                        file,
                        new FileWatch.Listener() {

                            @Override
                            public void onFileChanged(FileWatch fileWatch) {
                                log().d("update cfg properties file", file);
                                load();
                                MApi.getCfgUpdater().doUpdate(getName());
                            }

                            @Override
                            public void onFileWatchError(FileWatch fileWatch, Throwable t) {
                                log().d(file, t);
                            }
                        });
        fileWatch.doStart();
    }

    private void load() {
        try {
            config = M.l(IConfigFactory.class).read(file);
        } catch (MException e) {
            log().d(file, e);
        }
    }

    @Override
    public void doStop() {
        if (fileWatch != null) {
            fileWatch.doStop();
            fileWatch = null;
        }
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void doRestart() {
        doStop();
        doStart();
    }
}
