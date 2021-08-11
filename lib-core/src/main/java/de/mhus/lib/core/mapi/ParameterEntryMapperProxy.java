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
package de.mhus.lib.core.mapi;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.logging.ParameterEntryMapper;

public class ParameterEntryMapperProxy implements ParameterEntryMapper {

    private String clazz;
    private ParameterEntryMapper inst;
    private long last;

    public ParameterEntryMapperProxy(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object map(Object in) {
        if (inst == null && MPeriod.isTimeOut(last, MPeriod.MINUTE_IN_MILLISECOUNDS)) {
            last = System.currentTimeMillis();
            try {
                inst =
                        (ParameterEntryMapper)
                                MApi.get().createActivator().createObject(clazz.trim());
            } catch (Throwable t) {
                MApi.dirtyLogDebug("ParameterEntryMapperProxy", clazz, t.getMessage());
            }
        }
        return inst == null ? in : inst.map(in);
    }
}
