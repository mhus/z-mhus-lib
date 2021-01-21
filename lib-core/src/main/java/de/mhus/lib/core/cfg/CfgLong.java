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

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;

public class CfgLong extends CfgValue<Long> {

    public CfgLong(Object owner, String path, long def) {
        super(owner, path, def);
    }

    @Override
    protected Long loadValue() {
        IConfig node = getNode();
        if (node == null) return getDefault();
        return node.getLong(getParameterName(), getDefault());
    }

    @Override
    protected Long loadValue(String value) {
        return MCast.tolong(value, 0);
    }
}
