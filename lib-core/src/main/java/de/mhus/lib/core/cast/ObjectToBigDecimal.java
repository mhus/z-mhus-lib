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
package de.mhus.lib.core.cast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

public class ObjectToBigDecimal implements Caster<Object, BigDecimal> {

    @Override
    public Class<? extends BigDecimal> getToClass() {
        return BigDecimal.class;
    }

    @Override
    public Class<? extends Object> getFromClass() {
        return Object.class;
    }

    @Override
    public BigDecimal cast(Object in, BigDecimal def) {
        return cast(in, def, Locale.getDefault());
    }

    public BigDecimal cast(Object in, BigDecimal def, Locale locale) {
        if (in == null) return def;
        try {
            if (in instanceof BigDecimal) return (BigDecimal) in;
            if (in instanceof Long) return new BigDecimal((Long) in);
            if (in instanceof Integer) return new BigDecimal((Integer) in);
            if (in instanceof Double) return new BigDecimal((Double) in);
            if (in instanceof BigInteger) return new BigDecimal((BigInteger) in);
            if (in instanceof String) return new BigDecimal((String) in);

            String ins = String.valueOf(in);
            return new BigDecimal(ins);
        } catch (Throwable t) {
            return def;
        }
    }
}
