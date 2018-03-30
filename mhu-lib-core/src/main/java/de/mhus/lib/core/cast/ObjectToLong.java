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
package de.mhus.lib.core.cast;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.ObjectContainer;

public class ObjectToLong implements Caster<Object,Long> {

	private final static Log log = Log.getLog(ObjectToLong.class);
	
	@Override
	public Class<? extends Long> getToClass() {
		return Long.class;
	}

	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	@Override
	public Long cast(Object in, Long def) {
		ObjectContainer<Long> ret = new ObjectContainer<>(def);
		toLong(in, 0, ret);
		return ret.getObject();
	}

	public long toLong(Object in, long def, ObjectContainer<Long> ret) {
		if (in == null) return def;
		if (in instanceof Number) {
			long r = ((Number)in).longValue();
			if (ret != null) ret.setObject(r);
			return r;
		}
		String ins = String.valueOf(in);
		
		try {
			
			if (ins.startsWith("0x") || ins.startsWith("-0x") || ins.startsWith("+0x")) {
				int start = 2;
				if (ins.startsWith("-")) start = 3;
				long out = 0;
				for (int i = start; i < ins.length(); i++) {
					int s = -1;
					char c = ins.charAt(i);
					if (c >= '0' && c <= '9')
						s = c - '0';
					else if (c >= 'a' && c <= 'f')
						s = c - 'a' + 10;
					else if (c >= 'A' && c <= 'F')
						s = c - 'A' + 10;

					if (s == -1)
						throw new NumberFormatException(ins);
					out = out * 16 + s;
				}
				if (ins.startsWith("-")) out = -out;
				if (ret != null) ret.setObject(out);
				return out;
			}
			
			long r = Long.parseLong(ins);
			if (ret != null) ret.setObject(r);
			return r;
			
		} catch (Throwable e) {
			log.t(ins, e.toString());
		}
		return def;
	}
	
}
