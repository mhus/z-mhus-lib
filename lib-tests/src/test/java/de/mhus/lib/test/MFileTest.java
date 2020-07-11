/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MFile;
import de.mhus.lib.errors.MException;
import de.mhus.lib.tests.TestCase;

public class MFileTest extends TestCase {

    @Test
    public void testSaveLoadHistory() throws MException, IOException {
    	LinkedList<String> history = new LinkedList<>();
    	history.add("first");
    	history.add("multi\nline");
    	history.add("last");
    	System.out.println(history);
    	
    	File file = new File("target/history.lines");
    	MFile.writeLinesEncoded(file, history, false);
    	
    	List<String> copy = MFile.readLinesEncoded(file, true);
    	System.out.println(copy);
    	assertEquals(history.size(), copy.size());
    	assertEquals(history.get(0), copy.get(0));
    	assertEquals(history.get(history.size()-1), copy.get(history.size()-1));
    	
    }
    
    @Test
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
