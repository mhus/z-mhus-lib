package de.mhus.lib.test;

import java.util.Locale;

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

		Locale l = new Locale("de","DE");
		assertEquals(true, MValidator.isZipCode(l, "04212"));
		assertEquals(true, MValidator.isZipCode(l, "95030"));
		assertEquals(false, MValidator.isZipCode(l,"00999"));
		assertEquals(false, MValidator.isZipCode(l,"100000"));
		assertEquals(false, MValidator.isZipCode(l,"2000"));
		l = new Locale("en","US");
		try {
			MValidator.isZipCode(l, "04212");
			assertEquals(true, false);
		} catch (Throwable t) {
		}
	}
}
