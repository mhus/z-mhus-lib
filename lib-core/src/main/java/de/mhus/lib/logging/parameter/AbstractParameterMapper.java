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

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.ParameterMapper;

public abstract class AbstractParameterMapper implements ParameterMapper {

    @Override
    public Object[] map(Log log, Object[] msg) {
        if (msg == null) return msg;
        for (int i = 0; i < msg.length; i++) {
            Object o = msg[i];
            if (o == null) continue;
            o = map(o);
            if (o != null) msg[i] = o;
        }
        return msg;
    }

    protected abstract Object map(Object o);
}
