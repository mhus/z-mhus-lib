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
import java.util.regex.Pattern;

public class ModelRegex extends ModelPattern {

    private Pattern pattern;

    @Override
    public boolean matches(ModelPart model, Map<String, ?> map, String str) {
        setCondition(CONDITION.NONE);
        return pattern.matcher(str).matches();
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public String getPattern() {
        return pattern.toString();
    }

    @Override
    public String getPatternStr() {
        return "'" + pattern.toString().replace("'", "\\'") + "'";
    }

    @Override
    public String getPatternTypeName() {
        return "regex";
    }

    @Override
    public void setCondition(CONDITION cond) {}
}
