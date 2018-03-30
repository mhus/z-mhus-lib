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
