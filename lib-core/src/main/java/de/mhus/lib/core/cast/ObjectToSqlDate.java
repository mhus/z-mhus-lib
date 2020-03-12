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
package de.mhus.lib.core.cast;

import java.sql.Date;

import de.mhus.lib.core.MCast;

public class ObjectToSqlDate implements Caster<Object, Date> {

    @Override
    public Class<? extends Date> getToClass() {
        return Date.class;
    }

    @Override
    public Class<? extends Object> getFromClass() {
        return Object.class;
    }

    @Override
    public Date cast(Object in, Date def) {
        if (in == null) return def;
        try {
            String ins = MCast.toString(in);
            return MCast.toSqlDate(MCast.toDate(ins, def));
        } catch (Throwable t) {
            return def;
        }
    }
}
