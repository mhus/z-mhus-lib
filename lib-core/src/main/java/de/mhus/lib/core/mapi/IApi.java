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
package de.mhus.lib.core.mapi;

import java.io.File;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.util.BaseControl;

public interface IApi {

    // Log createLog(Object owner);

    MCfgManager getCfgManager();

    BaseControl getBaseControl();

    MActivator createActivator();

    LogFactory getLogFactory();

    boolean isTrace(String name);

    MBase base();

    /**
     * Return a File inside the current application context.
     *
     * @param scope Where to locate the requested file name.
     * @param name The name / path of the file or directory inside the scope
     * @return file The file.
     */
    File getFile(MApi.SCOPE scope, String name);

    Log lookupLog(Object owner);

    void updateLog();

    default String getCfgString(Class<?> owner, String path, String def) {
        int p = path.indexOf('@');
        if (p < 0) return MApi.getCfg(owner).getString(path, def);
        IConfig node = MApi.getCfg(owner).getObjectByPath(path.substring(0, p));
        if (node == null) return def;
        return node.getString(path.substring(p + 1), def);
    }
}
