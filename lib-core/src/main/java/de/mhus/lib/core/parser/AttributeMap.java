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
package de.mhus.lib.core.parser;

import java.util.HashMap;

public class AttributeMap extends HashMap<String, Object> {

    /** */
    private static final long serialVersionUID = 8679000665603785877L;

    public AttributeMap() {}

    public AttributeMap(Object... entries) {
        if (entries == null) return;
        for (int i = 0; i < entries.length; i += 2) {
            put(String.valueOf(entries[i]), entries[i + 1]);
        }
    }
}
