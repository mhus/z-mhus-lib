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
package de.mhus.lib.core.activator;

public interface MutableActivator {

	void addObject(Class<?> ifc, String name, Object obj);
	void addMap(Class<?> ifc, Class<?> clazz);
	void addMap(Class<?> ifc, String name, Class<?> clazz);
	void addMap(String name, Class<?> clazz);
	void removeMap(String name);
	void removeObject(String name);
	void removeObject(Class<?> ifc, String name);
	String[] getMapNames();
	String[] getObjectNames();

}
