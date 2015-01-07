package de.mhus.lib.test;

import junit.framework.TestCase;
import de.mhus.lib.core.MString;

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
}
