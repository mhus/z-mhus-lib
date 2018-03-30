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
package de.mhus.lib.core.strategy;

import java.util.HashMap;
import java.util.Set;

public class SuccessfulMap extends Successful {

	public SuccessfulMap(Operation operation, String msg) {
		super(operation, msg);
		setResult(new HashMap<>());
	}

	public SuccessfulMap(String path, String msg, long rc) {
		super(path, msg, rc, new HashMap<>());
	}

	public SuccessfulMap(String path, String msg, long rc, String... keyValues) {
		super(path, msg, rc, keyValues);
	}

	@SuppressWarnings("unchecked")
	public void put(String key, Object value) {
		((HashMap<String,Object>)getResult()).put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public Object get(String key) {
		return ((HashMap<String,Object>)getResult()).get(key);
	}
	
	@SuppressWarnings("unchecked")
	public void remove(String key) {
		((HashMap<String,Object>)getResult()).remove(key);
	}

	@SuppressWarnings("unchecked")
	public Set<String> keySet() {
		return ((HashMap<String,String>)getResult()).keySet();
	}

	@SuppressWarnings("unchecked")
	public int size() {
		return ((HashMap<String,String>)getResult()).size();
	}
	
}
