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
package de.mhus.lib.core.lang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Used to mark a string value as - not a string - example in the SimpleQueryCompiler.
 *
 * @author mikehummel
 */
public class Raw implements Serializable {

    private static final long serialVersionUID = -7999465299200662577L;

    private String value;

    public Raw(String value) {
        this.value = value;
    }

    public Raw() {}

    @Override
    public String toString() {
        return value;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        //		out.writeUTF(value);
        out.writeObject(value);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        //		value = in.readUTF();
        value = (String) in.readObject();
    }
}
