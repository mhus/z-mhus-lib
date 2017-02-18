package de.mhus.lib.test;

import java.util.Arrays;

import de.mhus.lib.core.util.MUri;
import junit.framework.TestCase;

public class Rfc1738Test extends TestCase {

	public void testCoding() {
		String s = "abcdefghijklmnop1234567890 -_+=&12;:.....\u1123";
		String d = MUri.decode(MUri.encode(s));
		assertTrue(s.equals(d));
	}
	
	public void testArrays() {
		String[] s = new String[] { "abc", "def", "123454" };
		String[] d = MUri.explodeArray(MUri.implodeArray(s));
		assertTrue(Arrays.equals(s, d));
		
		s = new String[] {};
		d = MUri.explodeArray(MUri.implodeArray(s));
		assertTrue(Arrays.equals(s, d));
		
	}
	
}
