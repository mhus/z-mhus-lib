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
package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ADynValue class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ADynValue extends AAttribute {

	private String name;
	private Object value;

	/**
	 * <p>Constructor for ADynValue.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public ADynValue(Object value) {
		this(null, value);
	}

	/**
	 * <p>Constructor for ADynValue.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public ADynValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AQuery<?> query, AttributeMap map) {
		if (name == null)
			name = "v" + query.nextUnique();
		map.put(name, value);
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "dyn:[" + value + "]";
	}

	public String getName() {
		return name;
	}
}
