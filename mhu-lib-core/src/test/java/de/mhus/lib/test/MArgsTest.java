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

import de.mhus.lib.core.MArgs;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MArgsTest {

	@Test
	public void testDefaultParsing() {
		String[] args = new String[] { "a", "b", "c" };
		MArgs ma = new MArgs(args);
		assertTrue( ma.getKeys().size() == 1 );
		assertTrue( ma.getValues(MArgs.DEFAULT).length == 3 );
	}
	
	@Test
	public void testKeyParsing() {
		String[] args = new String[] { "-a", "aa", "-a", "bb", "-a", "cc" };
		MArgs ma = new MArgs(args);
		assertTrue( ma.getKeys().size() == 1 );
		assertTrue( ma.getValues("a").length == 3 );
	}
	
	@Test
	public void testMultiParsing() {
		String[] args = new String[] { "default", "-a", "aa", "default", "-a", "bb", "-b", "cc" };
		MArgs ma = new MArgs(args);
		assertTrue( ma.getKeys().size() == 3 );
		assertTrue( ma.getValues(MArgs.DEFAULT).length == 2 );
		assertTrue( ma.getValues("a").length == 2 );
		assertTrue( ma.getValues("b").length == 1 );
	}
	
	@Test
	public void testOrder() {
		String[] args = new String[] { "-a", "zz", "-a", "bb", "-a", "aa" };
		MArgs ma = new MArgs(args);
		assertTrue( ma.getKeys().size() == 1 );
		assertTrue( ma.getValues("a").length == 3 );
		assertTrue( ma.getValue("a", 0).equals( "zz" ) );
		assertTrue( ma.getValue("a", 1).equals( "bb" ) );
		assertTrue( ma.getValue("a", 2).equals( "aa" ) );
	}
	
}
