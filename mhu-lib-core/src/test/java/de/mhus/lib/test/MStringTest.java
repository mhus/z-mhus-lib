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

import de.mhus.lib.core.MString;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MStringTest {

	@Test
	public void testInitials() {
		String title = " ";
		String res = MString.findInitials(title, true, true, 0);
		assertEquals("?", res);

		title = " aber Hallo , wer hat denn hier 51 Hasen.";
		
		res = MString.findInitials(title, true, false, 0);
		assertEquals("HH", res);
		
		res = MString.findInitials(title, false, false, 0);
		assertEquals("aHwhdhH", res);
		
		res = MString.findInitials(title, false, false, 3);
		assertEquals("aHw", res);

		res = MString.findInitials(title, false, true, 0);
		assertEquals("aHwhdh5H", res);

	}
	
	@Test
	public void testEncodeUnicode() {
		{
			String test = "Herr Müller";
			String code = MString.encodeUnicode(test);
			System.out.println(code);
			assertEquals("Herr M\\u00fcller", code);
			String deco = MString.decodeUnicode(code);
			assertEquals(test, deco);
		}
		
		{
			String test = "Herr Müller\nFrau Müller";
			String code = MString.encodeUnicode(test);
			System.out.println(code);
			assertEquals("Herr M\\u00fcller\nFrau M\\u00fcller", code);
			String deco = MString.decodeUnicode(code);
			assertEquals(test, deco);
		}

		{
			String test = "Herr Müller\nFrau Müller";
			String code = MString.encodeUnicode(test, true);
			System.out.println(code);
			assertEquals("Herr M\\u00fcller\\u000aFrau M\\u00fcller", code);
			String deco = MString.decodeUnicode(code);
			assertEquals(test, deco);
		}
		
	}
}
