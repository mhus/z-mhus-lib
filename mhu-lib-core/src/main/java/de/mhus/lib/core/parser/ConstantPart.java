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
package de.mhus.lib.core.parser;

import java.util.Map;

import de.mhus.lib.core.MString;

public class ConstantPart implements ParsingPart {

	private String content;

	public ConstantPart(String content) {
		this.content = content;
	}
	
	@Override
	public void execute(StringBuilder out, Map<String, Object> attributes) {
		out.append(content);
	}

	@Override
	public void parse(ParseReader str) throws ParseException {
	}

	@Override
	public void dump(int level, StringBuilder out) {
		MString.appendRepeating(level, ' ', out);
		out.append(getClass().getCanonicalName()).append(content);
	}


}
