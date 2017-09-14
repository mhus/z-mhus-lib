package de.mhus.lib.test;

import de.mhus.lib.core.MString;
import junit.framework.TestCase;

public class MStringTest extends TestCase {

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
