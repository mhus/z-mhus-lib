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
package de.mhus.lib.core.matcher;

import java.util.Map;

import de.mhus.lib.core.util.Version;
import de.mhus.lib.core.util.VersionRange;

public class ModelRange extends ModelPattern {

    private VersionRange pattern;

    @Override
    public boolean matches(ModelPart model, Map<String, ?> map, String str) {
        setCondition(CONDITION.NONE);
        Version v = new Version(str);
        return pattern.includes(v);
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = new VersionRange(pattern);
    }

    @Override
    public String getPattern() {
        return pattern.toString();
    }

    @Override
    public String getPatternStr() {
        return "'" + pattern.toString() + "'";
    }

    @Override
    public String getPatternTypeName() {
        return "range";
    }

    @Override
    public void setCondition(CONDITION cond) {}
}
