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

import de.mhus.lib.core.util.Value;

public class ObjectToBoolean implements Caster<Object, Boolean> {

    @Override
    public Class<? extends Boolean> getToClass() {
        return Boolean.class;
    }

    @Override
    public Class<? extends Object> getFromClass() {
        return Object.class;
    }

    @Override
    public Boolean cast(Object in, Boolean def) {
        Value<Boolean> ret = new Value<>(def);
        toBoolean(in, false, ret);
        return ret.getValue();
    }

    public boolean toBoolean(Object in, boolean def, Value<Boolean> ret) {
        if (in == null) return def;

        if (in instanceof Boolean) return (Boolean) in;

        if (in instanceof Number) return !(((Number) in).intValue() == 0);

        String ins = in.toString().toLowerCase().trim();

        if (ins.equals("yes")
                || ins.equals("on")
                || ins.equals("true")
                || ins.equals("ja") // :-)
                || ins.equals("tak") // :-)
                || ins.equals("oui") // :-)
                || ins.equals("si") // :-)
                || ins.equals("\u4fc2") // :-) chinese
                || ins.equals("HIja'") // :-) // klingon
                || ins.equals("1")
                || ins.equals("t")
                || ins.equals("y")
                || ins.equals("\u2612")) {
            if (ret != null) ret.setValue(true);
            return true;
        }

        if (ins.equals("no")
                || ins.equals("off")
                || ins.equals("false")
                || ins.equals("nein") // :-)
                || ins.equals("nie") // :-)
                || ins.equals("non") // :-)
                || ins.equals("\u5514\u4fc2") // :-) chinese
                || ins.equals("Qo'") // :-) klingon
                || ins.equals("0")
                || ins.equals("-1")
                || ins.equals("f")
                || ins.equals("n")
                || ins.equals("\u2610")) {
            if (ret != null) ret.setValue(false);
            return false;
        }
        return def;
    }
}
