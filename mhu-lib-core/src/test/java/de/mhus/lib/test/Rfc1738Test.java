package de.mhus.lib.test;

import java.util.Arrays;

import junit.framework.TestCase;
import de.mhus.lib.core.util.Rfc1738;

public class Rfc1738Test extends TestCase {

	public void testCoding() {
		String s = "abcdefghijklmnop1234567890 -_+=&12;:.....\u1123";
		String d = Rfc1738.decode(Rfc1738.encode(s));
		assertTrue(s.equals(d));
	}
	
	public void testArrays() {
		String[] s = new String[] { "abc", "def", "123454" };
		String[] d = Rfc1738.explodeArray(Rfc1738.implodeArray(s));
		assertTrue(Arrays.equals(s, d));
		
		s = new String[] {};
		d = Rfc1738.explodeArray(Rfc1738.implodeArray(s));
		assertTrue(Arrays.equals(s, d));
		
	}
	
}
