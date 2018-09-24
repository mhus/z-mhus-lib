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
package de.mhus.lib.core.console;

public class ConsoleKey {

	public static final char F1 = 1;
	public static final char F2 = 2;
	public static final char F3 = 3;
	public static final char F4 = 4;
	public static final char F5 = 5;
	public static final char F6 = 6;
	public static final char F7 = 7;
	public static final char F8 = 8;
	public static final char F9 = 9;
	public static final char F10 = 10;
	public static final char F11 = 11;
	public static final char F12 = 12;
	
	public static final char SPECIAL_LEFT=68;
	public static final char SPECIAL_UP=65;
	public static final char SPECIAL_DOWN=66;
	public static final char SPECIAL_RIGHT=67;
	public static final char ENTER=13;

	public static final byte ALT     = 1;
	public static final byte CONTROL = 2;
	public static final byte SYSTEM  = 4;
	
	private boolean special;
	private char key;
	private byte control = 0;
	
	public ConsoleKey(byte control, boolean special, char key) {
		this.control = control;
		this.special = special;
		this.key = key;
	}

	public boolean isSpecial() {
		return special;
	}

	public char getKey() {
		return key;
	}
	
	public boolean isAlt() {
		return control % 2 == 1;
	}

	public boolean isControl() {
		return control / 2 % 2 == 1;
	}

	public boolean isSystem() {
		return control / 4 % 2 == 1;
	}

}
