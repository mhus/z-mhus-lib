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

import org.junit.Test;

import de.mhus.lib.core.crypt.pem.PemBlockList;
import de.mhus.lib.core.crypt.pem.PemBlockModel;
import de.mhus.lib.core.parser.ParseException;
import junit.framework.TestCase;

public class PemBlockTest extends TestCase {

	@Test
	public void testBlock() throws ParseException {
		
		{
			
			String longText = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";
			
			String text = "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r"
					+ "Aloa Mr. Nobody\n\r";
			PemBlockModel b = new PemBlockModel("test");
			b.setString("Version", "1.3.4");
			b.setString("Method", "TEST-10");
			b.setString("Long", longText);
			b.setBlock(text);
			
			String block = b.toString();
			System.out.println(block);
			
			PemBlockModel e = new PemBlockModel().parse(block);
			
			System.out.println(e);
			
			assertEquals("TEST", e.getName());
			assertEquals("1.3.4", e.getString("Version",null));
			assertEquals("TEST-10", e.getString("Method",null));
			assertEquals(longText, e.getString("Long", null));
			assertEquals(text, e.getBlock());
			
		}
		
	}
	
	@Test
	public void testBlockList() throws ParseException {
		{
			PemBlockList list = new PemBlockList();
			list.add(new PemBlockModel("TEST1").set("Version", "1.2.3").setBlock("ASDFGHJKLQWERTYUIOPZXCVBNMASDFGHJKLQWERTYUIOPZXCVBNMASDFGHJKLQWERTYUIOPZXCVBNM") );
			list.add(new PemBlockModel("TEST2").set("Version", "1.2.3").setBlock("asdfghjklqwertyuiopzxcvbnmasdfghjklqwertyuiopzxcvbnmasdfghjklqwertyuiopzxcvbnm") );
			
			String blocksStr = list.toString();
			
			System.out.println(blocksStr);
			
			PemBlockList list2 = new PemBlockList(blocksStr);
			
			assertEquals(2, list2.size());
			assertEquals("TEST1", list2.get(0).getName());
			assertEquals("TEST2", list2.get(1).getName());
			
		}
	}

}
