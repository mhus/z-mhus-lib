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

import de.mhus.lib.core.MString;

public class ModelFs extends ModelPattern {

    private String pattern;

    @Override
    public void setPattern(String pattern) {
        setCondition(CONDITION.NONE);
        this.pattern = pattern;
    }

    @Override
    protected boolean matches(ModelPart model, Map<String, ?> map, String str) {
        return MString.compareFsLikePattern(str, pattern);
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public String getPatternStr() {
        return "'" + pattern.toString().replace("'", "\\'") + "'";
    }

    @Override
    public String getPatternTypeName() {
        return "fs";
    }

    @Override
    public void setCondition(CONDITION cond) {}
}
