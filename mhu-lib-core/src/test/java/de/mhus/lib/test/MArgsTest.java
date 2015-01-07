package de.mhus.lib.test;

import junit.framework.TestCase;
import de.mhus.lib.core.MArgs;

public class MArgsTest extends TestCase {

	public void testDefaultParsing() {
		String[] args = new String[] { "a", "b", "c" };
		MArgs ma = new MArgs(args);
		assertTrue( ma.getKeys().size() == 1 );
		assertTrue( ma.getValues(MArgs.DEFAULT).length == 3 );
	}
	
	public void testKeyParsing() {
		String[] args = new String[] { "-a", "aa", "-a", "bb", "-a", "cc" };
		MArgs ma = new MArgs(args);
		assertTrue( ma.getKeys().size() == 1 );
		assertTrue( ma.getValues("a").length == 3 );
	}
	
	public void testMultiParsing() {
		String[] args = new String[] { "default", "-a", "aa", "default", "-a", "bb", "-b", "cc" };
		MArgs ma = new MArgs(args);
		assertTrue( ma.getKeys().size() == 3 );
		assertTrue( ma.getValues(MArgs.DEFAULT).length == 2 );
		assertTrue( ma.getValues("a").length == 2 );
		assertTrue( ma.getValues("b").length == 1 );
	}
	
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
