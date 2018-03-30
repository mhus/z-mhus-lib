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
package de.mhus.lib.test;

import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MCollection;
import junit.framework.TestCase;

public class MCollectionTest extends TestCase {

	public void testListSort() {
		LinkedList<String> l = new LinkedList<>();
		l.add("z");
		l.add("a");
		l.add("m");
		List<String> s = MCollection.sorted(l);
		assertEquals("a", s.get(0));
		assertEquals("m", s.get(1));
		assertEquals("z", s.get(2));
	}
	
	public void testStringList() {
		String list = null;
		char S = ',';
		list = MCollection.append(list, S, "a");
		assertEquals("a", list);
		list = MCollection.append(list, S, "b");
		assertEquals("a,b", list);
		list = MCollection.append(list, S, "c");
		assertEquals("a,b,c", list);
		
		list = MCollection.set(list, S, "a");
		assertEquals("a,b,c", list);
		list = MCollection.set(list, S, "b");
		assertEquals("a,b,c", list);
		list = MCollection.set(list, S, "c");
		assertEquals("a,b,c", list);
		
		list = MCollection.remove(list, S, "b");
		assertEquals("a,c", list);
		list = MCollection.remove(list, S, "a");
		assertEquals("c", list);
		list = MCollection.remove(list, S, "c");
		assertEquals("", list);
		
	}
	
}
