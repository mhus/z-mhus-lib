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
package de.mhus.lib.core.cast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ObjectToDate implements Caster<Object, Date> {

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
        return cast(in, def, Locale.getDefault());
    }

    public Date cast(Object in, Date def, Locale locale) {
        if (in == null) return def;
        if (in instanceof Date) return (Date) in;
        if (in instanceof Calendar) return ((Calendar) in).getTime();
        if (in instanceof Long) return new Date((Long) in);
        try {
            String ins = String.valueOf(in);
            Calendar c = ObjectToCalendar.toCalendar(ins, locale);
            if (c == null) return def;
            return c.getTime();
        } catch (Throwable t) {
            return def;
        }
    }
}
