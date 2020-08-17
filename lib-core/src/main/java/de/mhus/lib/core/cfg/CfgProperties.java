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

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.util.PropertiesSubset;

public class CfgProperties extends CfgValue<IProperties> {

    public CfgProperties(Object owner, String path) {
        super(owner, path, new MProperties());
    }

    @Override
    protected IProperties loadValue() {

        IConfig node = MApi.getCfg(getOwner(), null);
        if (node == null) return getDefault();
        if (MString.isEmpty(getPath())) return node;

        return new PropertiesSubset(node, getPath());
    }

    @Override
    protected IProperties loadValue(String value) {
        return IProperties.explodeToMProperties(value.split("\n"));
    }
}
