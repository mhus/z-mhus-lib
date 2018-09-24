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
