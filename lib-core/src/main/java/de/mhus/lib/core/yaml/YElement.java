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
package de.mhus.lib.core.yaml;

import java.util.List;
import java.util.Map;

public class YElement {

    private Object obj;

    public YElement(Object elem) {
        this.obj = elem;
    }

    public YMap asMap() {
        return new YMap(obj);
    }

    public YList asList() {
        return new YList(obj);
    }

    @Override
    public String toString() {
        return obj == null ? null : obj.toString();
    }

    //    public String getString() {
    //        return getString(null);
    //    }
    //
    //    public String getString(String def) {
    //        if (obj == null) return def;
    //        if (obj instanceof String) return (String) obj;
    //        return String.valueOf(obj);
    //    }
    //
    //    public boolean getBoolean() {
    //        return getBoolean(false);
    //    }
    //
    //    public boolean getBoolean(boolean def) {
    //        if (obj == null) return def;
    //        if (obj instanceof Boolean) return (Boolean) obj;
    //        return MCast.toboolean(obj, def);
    //    }
    //
    //    public int getInteger() {
    //        return getInteger(0);
    //    }
    //
    //    public int getInteger(int def) {
    //        if (obj == null) return def;
    //        if (obj instanceof Number) return ((Number) obj).intValue();
    //        return MCast.toint(obj, def);
    //    }

    //    public boolean isInteger() {
    //        if (obj == null) return false;
    //        return obj instanceof Number;
    //    }
    //
    //    public boolean isString() {
    //        return obj instanceof String;
    //    }

    public boolean isList() {
        return obj instanceof List;
    }

    public boolean isMap() {
        return obj instanceof Map;
    }

    public Object getObject() {
        return obj;
    }
}
