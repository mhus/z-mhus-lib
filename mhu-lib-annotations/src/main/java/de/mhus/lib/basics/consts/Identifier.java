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
package de.mhus.lib.basics.consts;

/**
 * Represents a identifier
 * 
 * @author mikehummel
 *
 */
public class Identifier {

	private String id;
	private Class<?> clazz;

	public Identifier(String id) {
		this.id = id;
	}
	
	public Identifier(Class<?> clazz, String id) {
		this.id = id;
		this.clazz = clazz;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	public String getPojoName() {
		return id.toLowerCase();
	}
	
	@Override
	public boolean equals(Object other) {
		return id.equals(String.valueOf(other));
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
}
