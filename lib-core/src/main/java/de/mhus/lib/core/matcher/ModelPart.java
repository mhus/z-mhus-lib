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
package de.mhus.lib.core.matcher;

import java.util.Map;

import de.mhus.lib.basics.RC;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.core.parser.StringPart;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

public abstract class ModelPart {

    private boolean not;
    private String param;
    private StringPart extra;

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public boolean m(Map<String, ?> map, String str) {
        if (not) return !matches(this, null, str);
        else return matches(this, null, str);
    }

    public boolean m(Map<String, Object> map) {
        if (not) return !matches(map);
        else return matches(map);
    }

    public void setParamName(String param) {
        this.param = param;
        if (param != null && param.startsWith("#"))
            extra = StringCompiler.createExtraAttributePart(param);
    }

    public String getParamName() {
        return param;
    }

    protected boolean matches(Map<String, Object> map) {
        Object val = null;
        if (extra != null) {
            try {
                StringBuilder out = new StringBuilder();
                extra.execute(out, map);
                val = out.toString();
            } catch (MException e) {
                throw new MRuntimeException(RC.STATUS.ERROR, param, e);
            }
        } else val = map.get(param);
        if (val != null) return matches(this, map, String.valueOf(val));
        return false;
    }

    protected abstract boolean matches(ModelPart part, Map<String, ?> map, String str);
}
