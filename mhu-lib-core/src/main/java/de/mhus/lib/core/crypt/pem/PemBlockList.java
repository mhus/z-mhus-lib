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
package de.mhus.lib.core.crypt.pem;

import java.util.LinkedList;

import de.mhus.lib.core.parser.ParseException;

public class PemBlockList extends LinkedList<PemBlock> {

	private static final long serialVersionUID = 1L;

	public PemBlockList() {}
	
	public PemBlockList(String string) {
		while(true) {
			try {
				PemBlockModel next = new PemBlockModel().parse(string);
				add(next);
				string = next.getRest();
			} catch (ParseException e) {
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (PemBlock block : this) {
			b.append(block);
			//b.append('\n');
		}
		return b.toString();
	}

	public String toString(int offset, int len) {
		StringBuilder b = new StringBuilder();
		int cnt = 0;
		for (PemBlock block : this) {
			if (cnt >= offset+len) break;
			if (cnt >= offset)
				b.append(block);
			//b.append('\n');
			cnt++;
		}
		return b.toString();
	}

	public PemBlock find(String name) {
		for (PemBlock block : this) {
			if (name.equals(block.getName()))
				return block;
		}
		return null;
	}
	
}
