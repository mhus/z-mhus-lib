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
package de.mhus.lib.annotations.adb;

public class DbType {

    public static enum TYPE {
        INT,
        LONG,
        BOOL,
        DOUBLE,
        FLOAT,
        STRING,
        DATETIME,
        UUID,
        BLOB,
        UNKNOWN,
        BIGDECIMAL
    }

    private TYPE type;
    private int size;
    private String[] options;

    public DbType(TYPE type, int size, String[] options) {
        this.type = type;
        this.size = size;
        this.options = options;
    }

    public TYPE getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public String[] getOptions() {
        return options;
    }

    /**
     * Return the option corresponding to the index or the def value if this is not possible.
     *
     * @param index index of the option
     * @param def default value
     * @return option
     */
    public String getOption(int index, String def) {
        if (options == null || index < 0 || index >= options.length) return def;
        return options[index];
    }
}
