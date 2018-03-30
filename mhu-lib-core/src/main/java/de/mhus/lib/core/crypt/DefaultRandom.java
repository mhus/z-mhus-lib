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
package de.mhus.lib.core.crypt;

import java.security.SecureRandom;
import java.util.Random;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.MLogUtil;

public class DefaultRandom implements MRandom {

	private Random rand;
	private SecureRandom secureRandom;

	@Override
	public byte getByte() {
		return (byte)(Math.random() * 255);
	}

	@Override
	public int getInt() {
		return (int)(Math.random() * Integer.MAX_VALUE); // no negative values!
	}

	@Override
	public double getDouble() {
		return Math.random();
	}

	@Override
	public long getLong() {
		return (long)(Math.random() * Long.MAX_VALUE); // no negative values!
	}
	
	public synchronized Random getRandom() {
		if (rand == null)
			rand = new Random(getLong());
		return rand;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<? extends T> ifc) {
		if (Random.class.isAssignableFrom(ifc)) return (T)getRandom();
		return null;
	}

	@Override
	public char getChar() {
		return MString.CHARS_READABLE[ getInt() % MString.CHARS_READABLE.length ];
	}

	@Override
	public SecureRandom getSecureRandom() {
		try {
//			secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			secureRandom = SecureRandom.getInstanceStrong();
		} catch (Exception e) {
			MLogUtil.log().e(e);
		}
		return secureRandom;
	}

}
