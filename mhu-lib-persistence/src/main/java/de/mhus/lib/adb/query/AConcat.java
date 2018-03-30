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
 * <p>AConcat class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AConcat extends AAttribute {

	private AAttribute[] parts;

	/**
	 * <p>Constructor for AConcat.</p>
	 *
	 * @param parts a {@link de.mhus.lib.adb.query.AAttribute} object.
	 */
	public AConcat(AAttribute ... parts) {
		this.parts = parts;
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		for (AAttribute part : parts)
			part.getAttributes(map);
	}

	public AAttribute[] getParts() {
		return parts;
	}

}
