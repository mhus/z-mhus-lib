package de.mhus.lib.test;

import junit.framework.TestCase;
import de.mhus.lib.core.MPassword;

public class MPasswordTest extends TestCase {

	public void testPassword() {
		String pw = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_.:,;+*#<>";
		System.out.println(MPassword.encode(pw));
		assertTrue( pw.equals( MPassword.decode( MPassword.encode(pw) ) ) );
		
		pw = "test";
		System.out.println(MPassword.encode(pw));
		assertTrue( pw.equals( MPassword.decode( MPassword.encode(pw) ) ) );

	}
	
}
