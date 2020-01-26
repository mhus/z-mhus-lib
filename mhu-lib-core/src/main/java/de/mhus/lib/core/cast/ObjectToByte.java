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

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.ObjectContainer;

public class ObjectToByte implements Caster<Object, Byte> {

    private static final Log log = Log.getLog(ObjectToByte.class);

    @Override
    public Class<? extends Byte> getToClass() {
        return Byte.class;
    }

    @Override
    public Class<? extends Object> getFromClass() {
        return Object.class;
    }

    @Override
    public Byte cast(Object in, Byte def) {
        ObjectContainer<Byte> ret = new ObjectContainer<>(def);
        toByte(in, (byte) 0, ret);
        return ret.getObject();
    }

    public byte toByte(Object in, byte def, ObjectContainer<Byte> ret) {
        if (in == null) return def;
        if (in instanceof Byte) {
            if (ret != null) ret.setObject((Byte) in);
            return ((Byte) in).byteValue();
        }
        if (in instanceof Number) {
            byte r = ((Number) in).byteValue();
            if (ret != null) ret.setObject(r);
            return r;
        }

        String _in = String.valueOf(in);
        try {
            if (_in.startsWith("0x") || _in.startsWith("-0x") || _in.startsWith("+0x")) {
                int start = 2;
                if (_in.startsWith("-")) start = 3;
                int out = 0;
                for (int i = start; i < _in.length(); i++) {
                    int s = -1;
                    char c = _in.charAt(i);
                    if (c >= '0' && c <= '9') s = c - '0';
                    else if (c >= 'a' && c <= 'f') s = c - 'a' + 10;
                    else if (c >= 'A' && c <= 'F') s = c - 'A' + 10;

                    if (s == -1) throw new NumberFormatException(_in);
                    out = out * 16 + s;
                }
                if (_in.startsWith("-")) out = -out;
                if (out > Byte.MAX_VALUE) out = Byte.MAX_VALUE;
                if (out < Byte.MIN_VALUE) out = Byte.MIN_VALUE;
                if (ret != null) ret.setObject((byte) out);
                return (byte) out;
            }

            byte r = Byte.parseByte(_in);
            if (ret != null) ret.setObject(r);
            return r;
        } catch (Throwable e) {
            log.t(_in, e.toString());
            return def;
        }
    }
}
