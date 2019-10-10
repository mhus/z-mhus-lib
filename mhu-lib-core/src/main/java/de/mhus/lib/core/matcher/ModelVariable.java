/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.matcher;

import java.util.Map;

import de.mhus.lib.errors.MRuntimeException;

public class ModelVariable extends ModelPattern {

	private String name;

	@Override
	public void setPattern(String pattern) {
	    if (pattern.startsWith("${") && pattern.endsWith("}"))
	        pattern = pattern.substring(2,pattern.length()-1);
		this.name = pattern;
	}

	@Override
	protected boolean matches(ModelPart model, Map<String,?> map, String str) {
	    if (map == null) throw new MRuntimeException("variables not available, use condition not matcher");
	    Object val = map.get(name);
	    if (val == null) return false;
	    int c = str.compareTo(val.toString());
        switch (getCondition()) {
        case EQ:
            return c == 0;
        case GE:
            return c >= 0;
        case GR:
            return c > 0;
        case LE:
            return c <= 0;
        case LT:
            return c < 0;
        default:
            break;
        }
        return false;
	}

	@Override
	public String getPattern() {
		return name;
	}

    @Override
    public String getPatternStr() {
        return "${" + name + "}";
    }

	@Override
	public String getPatternTypeName() {
		return "var";
	}

}
