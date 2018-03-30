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
package de.mhus.lib.core.parser;

import java.util.Iterator;

/**
 * Parse a string 
 * @author mikehummel
 *
 */
public class StringTokenizerParser implements Iterable<String>, Iterator<String> {

	private String part;
	private String condition;
	protected String breakableCharacters = "";
	protected String enclosureCharacters = "\"'";
	protected String whiteSpace = " \r\t";
	protected String encapsulateCharacters = "\\";
	protected String lineSeparator = "\n";
	protected char enclosure;
	protected char encapsulated;
	protected StringBuilder buffer;
	private int line = 0;
	private char current;
	private String original;
	private boolean wasEnclosured;
	
	public StringTokenizerParser(String condition) {
		original = condition;
		condition = condition.trim();
		this.condition = condition;
	}

	public void parseToken() {

		while (true) {
			findNextToken();
			if (part == null) return;
			
			part = part.trim();
			if (part.length() != 0) break;
		}
		
	}

	protected void findNextToken() {
		
		part = null;
		if (condition == null) return;
		
		enclosure = 0;
		encapsulated = 0;
		buffer = new StringBuilder();
		for (int i = 0; i < condition.length(); i++) {
			current = condition.charAt(i);
			
			if (encapsulated != 0) {
				foundEncapsulated();
			} else {
				if (enclosure != 0 && current == enclosure) {
					foundToken(i);
					return;
				} else
				if (enclosure != 0) {
					foundCharacter();
				} else
				if (isEnclosureCharacter()) {
					enclosure = current;
				} else
				if (isWhiteSpace()) {
					foundToken(i);
					return;
				} else
				if (isEndOfLine()) {
					line++;
					foundToken(i);
					return;
				} else
				if (isEncapsulateStarting())
					encapsulated = current;
				else
				if (breakOnThisCharacter()) {
					foundBreak(i);
					return;
				} else
					foundCharacter();
			}
		}
		foundEnd();
	}

	protected void foundBreak(int i) {
		if (i == 0) {
			buffer.append(current);
			foundToken(i);
		} else {
			foundToken(i-1);
		}
	}

	protected void foundEnd() {
		part = buffer.toString();
		condition = null;
	}

	protected void foundToken(int i) {
		part = buffer.toString();
		condition = condition.substring(i+1);
	}

	protected boolean isEncapsulateStarting() {
		return encapsulateCharacters.indexOf(current) >= 0;
	}

	protected void foundEncapsulated() {
		buffer.append(current);
		encapsulated = 0;
	}

	protected void foundCharacter() {
		buffer.append(current);
	}

	protected boolean isWhiteSpace() {
		return whiteSpace.indexOf(current) >= 0;
	}

	protected boolean isEndOfLine() {
		return lineSeparator.indexOf(current) >= 0;
	}
	
	protected boolean isEnclosureCharacter() {
		return enclosureCharacters.indexOf(current) >= 0;
	}

	protected boolean breakOnThisCharacter() {
		return breakableCharacters .indexOf(current) >= 0;
	}

	public String getBreakableCharacters() {
		return breakableCharacters;
	}

	public void setBreakableCharacters(String breakableCharacters) {
		this.breakableCharacters = breakableCharacters;
	}

	public String getEnclosureCharacters() {
		return enclosureCharacters;
	}

	public void setEnclosureCharacters(String enclosureCharacters) {
		this.enclosureCharacters = enclosureCharacters;
	}

	public String getWhiteSpace() {
		return whiteSpace;
	}

	public void setWhiteSpace(String whiteSpace) {
		this.whiteSpace = whiteSpace;
	}

	public String getEncapsulateCharacters() {
		return encapsulateCharacters;
	}

	public void setEncapsulateCharacters(String encapsulateCharacters) {
		this.encapsulateCharacters = encapsulateCharacters;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	@Override
	public boolean hasNext() {
		return condition != null;
	}

	@Override
	public Iterator<String> iterator() {
		//reset();
		return this;
	}

	protected void reset() {
		condition = original;
		part = null;
		enclosure = 0;
		encapsulated = 0;
		buffer = null;
		line = 0;
	}

	@Override
	public String next() {
		parseToken();
		return part;
	}

	@Override
	public void remove() {
		
	}
	
	public boolean isTokenEncapsulated() {
		return encapsulated != 0;
	}
	
	public boolean isEnclosuredToken() {
		return wasEnclosured;
	}
	
}
