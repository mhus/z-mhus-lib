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

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.IConfig;

public class CfgFile extends CfgValue<File> {

    public CfgFile(Object owner, String path, File def) {
        super(owner, path, def);
    }

    @Override
    protected File loadValue() {
        int p = getPath().indexOf('@');
        if (p < 0) {
            String str = MApi.getCfg(getOwner()).getExtracted(getPath(), null);
            if (str == null) return getDefault();
            return new File(str);
        }
        IConfig node = MApi.getCfg(getOwner()).getObjectByPath(getPath().substring(0, p));
        if (node == null) return getDefault();
        String str = node.getExtracted(getPath().substring(p + 1), null);
        if (str == null) return getDefault();
        return new File(str);
    }

    @Override
    protected File loadValue(String value) {
        return new File(value);
    }
}
