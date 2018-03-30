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
package de.mhus.lib.core.util;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.crypt.MCrypt;

public final class SecureString {

	private byte[] data;
	private int length;
	
	public SecureString(String data) {
		this.length = data == null ? 0 : data.length();
		if (data == null) return;
		this.data = MCrypt.obfuscate(MString.toBytes(data));
	}
	
	public String value() {
		if (data == null) return null;
		return MString.toString(MCrypt.unobfuscate(data));
	}
	
	public int length() {
		return length;
	}

	public boolean isNull() {
		return data == null;
	}
	
}
