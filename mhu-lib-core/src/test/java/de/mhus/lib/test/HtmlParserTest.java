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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.parser.HtmlParser;
import junit.framework.TestCase;

public class HtmlParserTest extends TestCase {

	public void testParser() throws IOException {
		HtmlParser parser = new HtmlParser();
		parser.setTrim(true);
		
		HtmlListener listener = new HtmlListener();
		
		Reader in = new InputStreamReader( MSystem.locateResource(this, getClass().getSimpleName() + ".xml").openStream() );
		
		parser.parse(in, listener);
		
		assertTrue(listener.pi.getFirst().equals("xml version=\"1.0\" encoding=\"ISO-8859-1\""));
		assertTrue(listener.note.getFirst().equals("Edited"));
		assertTrue(listener.open.size() == listener.close.size());
		assertTrue(listener.single.size() == 1);
		assertTrue(listener.text.getFirst().equals("You"));
		assertTrue(listener.open.get("body").get("color").equals("blue"));
		assertTrue(listener.open.get("body").get("align").equals("center"));
	}
	
	
	private class HtmlListener implements HtmlParser.Listener {

		private LinkedList<String> text = new LinkedList<String>();
		private LinkedList<String> note = new LinkedList<String>();
		private LinkedList<String> pi = new LinkedList<String>();
		private LinkedList<String> close = new LinkedList<String>();
		private Hashtable<String,Map<String, String>> single = new Hashtable<String,Map<String, String>>();
		private Hashtable<String,Map<String, String>> open = new Hashtable<String,Map<String, String>>();

		@Override
		public boolean foundText(String _text) {
			System.out.println("Text: " + _text);
			text.add(_text);
			return true;
		}

		@Override
		public boolean foundNote(String _text) {
			System.out.println("Note: " + _text);
			note.add(_text);
			return true;
		}

		@Override
		public boolean foundProcessorInstruction(String _text) {
			System.out.println("PI: " + _text);
			pi.add(_text);
			return true;
		}

		@Override
		public boolean foundTagClose(String _text) {
			System.out.println("Close: " + _text);
			close.add(_text);
			return true;
		}

		@Override
		public boolean foundSingleTag(String _text,
				Map<String, String> _params) {
			System.out.println("Single: " + _text + " " + _params);
			single.put(_text,_params);
			return true;
		}

		@Override
		public boolean foundTagOpen(String _text,
				Map<String, String> _params) {
			System.out.println("Open: " + _text + " " + _params);
			open.put(_text,_params);
			return true;
		}
		
	}
}
