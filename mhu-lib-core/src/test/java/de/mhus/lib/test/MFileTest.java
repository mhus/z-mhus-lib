package de.mhus.lib.test;

import de.mhus.lib.core.MFile;
import junit.framework.TestCase;

public class MFileTest extends TestCase {

	public void testMimeTypes() {
		{
			String res = MFile.getMimeType("pdf");
			assertEquals("application/pdf", res);
		}
		{
			String res = MFile.getMimeType("PDF");
			assertEquals("application/pdf", res);
		}
		{
			String res = MFile.getMimeType("file.pdf");
			assertEquals("application/pdf", res);
		}
		{
			String res = MFile.getMimeType("asdhjak");
			assertEquals("text/plain", res);
		}
		{
			String res = MFile.getMimeType("html");
			assertEquals("text/html", res);
		}
	}
	
}
