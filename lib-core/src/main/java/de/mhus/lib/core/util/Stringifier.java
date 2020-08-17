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
package de.mhus.lib.core.util;

import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

/**
 * Convert a object into string using pojo mechanism. The conversion is done if the method toString
 * is used for the first time.
 *
 * @author mikehummel
 */
public class Stringifier {

    private Object from;
    private String str;
    private int level;

    public Stringifier(Object in) {
        this(in, 1);
    }

    public Stringifier(Object in, int level) {
        this.from = in;
        this.level = level;
    }

    @Override
    public synchronized String toString() {
        if (str == null) {
            str = doStringify(from, 0);
        }
        return str;
    }

    private String doStringify(Object in, int level) {

        if (in == null) return "null";
        if (level >= this.level) return in.toString();

        StringBuilder out = new StringBuilder();
        out.append('{');
        try {

            out.append(in.getClass()).append(':');

            PojoModel model =
                    new PojoParser()
                            .parse(in, "_", null)
                            .filter(new DefaultFilter(true, false, true, true, true))
                            .getModel();
            boolean first = true;
            for (PojoAttribute<?> attr : model) {
                if (first) first = false;
                else out.append(',');
                try {
                    out.append(attr.getName())
                            .append('=')
                            .append(doStringify(attr.get(in), level + 1));
                } catch (Throwable t) {
                    out.append(t.toString());
                }
            }
        } catch (Throwable t) {
            out.append(t.toString());
        }
        out.append('}');
        return out.toString();
    }

    public static void stringifyArray(Object[] in) {
        if (in == null) return;
        for (int i = 0; i < in.length; i++) in[i] = stringifyWrap(in[i]);
    }

    public static Object stringifyWrap(Object o) {
        if (o == null) return null;
        Class<? extends Object> c = o.getClass();
        if (c.isPrimitive() || o instanceof String) return o;
        String cn = c.getName();
        if (cn.startsWith("java") || cn.startsWith("javax")) return o;
        return new Stringifier(o);
    }
}
