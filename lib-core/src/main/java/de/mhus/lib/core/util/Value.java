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
package de.mhus.lib.core.util;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;

import de.mhus.lib.basics.Valueable;
import de.mhus.lib.core.MSystem;

public class Value<T> implements Valueable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    public Value() {}

    public Value(T initial) {
        value = initial;
    }

    public T value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object in) {
        if (in != null && in instanceof Valueable)
            return MSystem.equals(value, ((Valueable<?>) in).getValue());
        return MSystem.equals(value, in);
    }

    @Override
    public T getValue() {
        return value;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (value == null) {
            out.writeInt(0);
            return;
        }
        if (value instanceof Serializable) {
            out.writeInt(1);
            out.writeObject(value);
            return;
        }

        throw new NotSerializableException();
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        value = null;
        int type = in.readInt();
        if (type == 1) value = (T) in.readObject();
    }

    public void setValue(T value) {
        this.value = value;
    }
}
