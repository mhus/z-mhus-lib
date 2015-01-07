package de.mhus.lib.test;

import junit.framework.TestCase;
import de.mhus.lib.core.MValidator;

public class MValidatorTest extends TestCase {

	public void testEMail() {
		assertEquals(false, MValidator.isEmailAddress(null));
		assertEquals(false, MValidator.isEmailAddress(""));
		assertEquals(false, MValidator.isEmailAddress("@mhus.de"));
		assertEquals(false, MValidator.isEmailAddress("mike@"));
		assertEquals(true, MValidator.isEmailAddress("mike@mhus.de"));
		assertEquals(false, MValidator.isEmailAddress("mike@alababa"));
		assertEquals(true, MValidator.isEmailAddress("mike@alababa.blabla.de"));
		assertEquals(true, MValidator.isEmailAddress("mike@alababa.aha.soso.local"));
	}
}
