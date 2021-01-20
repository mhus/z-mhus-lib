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
package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.config.IConfig;

public class CfgNode extends CfgValue<IConfig> {

    /**
     * Link an node of the configuration to this configuration object.
     *
     * @param owner
     * @param path Path or null/empty for the main node
     * @param def
     */
    public CfgNode(Object owner, String path, IConfig def) {
        super(owner, path, def);
    }

    @Override
    protected IConfig loadValue() {
        IConfig node = null;
        if (getPath().length() == 0) node = MApi.getCfg(getOwner(), null);
        else node = MApi.getCfg(getOwner()).getObjectByPath(getPath());
        if (node == null) return getDefault();
        return node;
    }

    @Override
    protected IConfig loadValue(String value) {
        return null;
    }
}
