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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;

public class ObjectToString implements Caster<Object, String> {

    @Override
    public Class<? extends String> getToClass() {
        return String.class;
    }

    @Override
    public Class<? extends Object> getFromClass() {
        return Object.class;
    }

    @Override
    public String cast(Object in, String def) {
        if (in == null) return def;
        if (in instanceof String) return (String) in;
        if (in instanceof Date) return MDate.toIso8601((Date) in);
        if (in instanceof Calendar) return MDate.toIso8601((Calendar) in);
        if (in instanceof Throwable) return MCast.toString((Throwable) in);
        if (in instanceof byte[]) return MCast.toString((byte[]) in);
        if (in.getClass().isArray()) Arrays.asList(in).toString();
        return String.valueOf(in);
    }
}
