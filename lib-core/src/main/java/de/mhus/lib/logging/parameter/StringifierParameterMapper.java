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
package de.mhus.lib.logging.parameter;

import de.mhus.lib.core.util.Stringifier;

public class StringifierParameterMapper extends AbstractParameterMapper {

    @Override
    protected Object map(Object o) {
        Class<? extends Object> c = o.getClass();
        if (c.isPrimitive() || o instanceof String) return null;
        String cn = c.getName();
        if (cn.startsWith("java") || cn.startsWith("javax")) return null;
        return new Stringifier(o);
    }
}
