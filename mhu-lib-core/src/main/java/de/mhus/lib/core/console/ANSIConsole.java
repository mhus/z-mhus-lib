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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import de.mhus.lib.core.logging.MLogUtil;
import jline.console.ConsoleReader;

// http://ascii-table.com/ansi-escape-sequences.php

public class ANSIConsole extends Console {

	public static final int KEY_SPECIAL_LEFT=68;
	public static final int KEY_SPECIAL_UP=65;
	public static final int KEY_SPECIAL_DOWN=66;
	public static final int KEY_SPECIAL_RIGHT=67;
	public static final int KEY_ENTER=13;
	
	public static final int KEY_q=113;
	public static final int KEY_a=97;
	public static final int KEY_z=112;
	public static final int KEY_A=65;
	public static final int KEY_Z=90;
	public static final int KEY_0=48;
	public static final int KEY_9=57;

	public static final int KEY_TAB=9;
	public static final int KEY_ESC=27;
	public static final int KEY_CTRL_A=1;
	public static final int KEY_CTRL_Z=26;
	public static final int KEY_FUNCTION=79;
	public static final int KEY_F1=80;
	public static final int KEY_F2=81;
	public static final int KEY_F3=82;
	public static final int KEY_F4=83;
//	public static final int KEY_F5=84;
//	public static final int KEY_F6=85;
//	public static final int KEY_F7=86;
//	public static final int KEY_F8=87;
//	public static final int KEY_F9=88;
//	public static final int KEY_F10=89;
//	public static final int KEY_F11=90;


	protected COLOR foreground;
	protected COLOR background;
	protected boolean blink;
	protected boolean bold;
//	protected int width = DEFAULT_WIDTH;
//	protected int height = DEFAULT_HEIGHT;
	protected ConsoleReader reader;
	
	protected int width = 0;
	protected int height = 0;
	protected boolean supportSize;
	
	public ANSIConsole() throws IOException {
		super();
        reader = new ConsoleReader();
		loadSettings();
	}

	protected void loadSettings() {
		int w = reader.getTerminal().getWidth();
		int h = reader.getTerminal().getHeight();
		if (w == 80 && h == 24) { // default size if size can't be recognized
			width = DEFAULT_WIDTH;
			height = DEFAULT_HEIGHT;
			supportSize = false;
		} else {
			width = 0;
			height = 0;
			supportSize = true;
		}
	}

	public ANSIConsole(InputStream in, PrintStream out, boolean flush, String charset)
			throws IOException {
		super(out, flush, charset);
        reader = new ConsoleReader();
        loadSettings();
	}

	public ANSIConsole(InputStream in, PrintStream out) throws IOException {
		super(out);
        reader = new ConsoleReader();
        loadSettings();
	}

	@Override
	public String readPassword() throws IOException {
		return reader.readLine('*');
	}

	@Override
	public String readLine(LinkedList<String> history) {
		try {
			return reader.readLine();
		} catch (IOException e) {
			MLogUtil.log().t(e);
		}
		return null;
//		return System.console().readLine();
	}


	@Override
	public int read() {
		try {
			return reader.readCharacter(true);
		} catch (IOException e) {
			MLogUtil.log().t(e);
		}
		return -1;
	}

	@Override
	public ConsoleKey readKey() {
		while (true) {
			int first = read();
			if (first == 1091) {
				int second = read();
//				if (second == )
				
				switch(second) {
				case KEY_SPECIAL_DOWN:
				case KEY_SPECIAL_LEFT:
				case KEY_SPECIAL_RIGHT:
				case KEY_SPECIAL_UP:
					return new ConsoleKey((byte)0, true, (char)second);
				case KEY_F1:
				case KEY_F2:
				case KEY_F3:
				case KEY_F4:
					return new ConsoleKey((byte)0, true, (char)(second-79));
				default:
				}
				continue;
			}
			return new ConsoleKey((byte)0, false, (char)first);
		}
	}
	

	@Override
	public boolean isSupportSize() {
		return supportSize;
	}

	@Override
	public int getWidth() {
		if (width > 0) return width;
		return reader.getTerminal().getWidth();
	}

	@Override
	public int getHeight() {
		if (height > 0) return height;
		return reader.getTerminal().getHeight();
	}

	@Override
	public boolean isSupportCursor() {
		return true;
	}

	@Override
	public void setCursor(int x, int y) {
		print( ansiSetCursor(x, y));
	}
	
	public static String ansiSetCursor(int x, int y) {
		return (char)27 + "[" + y + ";" + x + "H" ;
	}

	@Override
	public int getCursorX() {
		return -1;
	}

	@Override
	public int getCursorY() {
		return -1;
	}

	@Override
	public boolean isSupportColor() {
		return reader.getTerminal().isAnsiSupported();
	}

	@Override
	public void setColor(COLOR foreground, COLOR background) {
		this.foreground = foreground;
		this.background = background;
		
		if (isSupportColor()) {
			if (foreground != null && foreground != COLOR.UNKNOWN)
				print( ansiForeground(foreground));
			if (background != null && background != COLOR.UNKNOWN)
				print( ansiBackground(background));
		}		
	}
	
	public static String ansiForeground(COLOR color) {
		return (char)27 + "[3" + ansiColorValue(color) + "m";
	}
	
	public static String ansiBackground(COLOR color) {
		return (char)27 + "[4" + ansiColorValue(color) + "m";
	}

	@Override
	public COLOR getForegroundColor() {
		return foreground;
	}

	@Override
	public COLOR getBackgroundColor() {
		return background;
	}

	@Override
	public boolean isSupportBlink() {
		return isSupportColor();
	}

	@Override
	public void setBlink(boolean blink) {
		this.blink = blink;
		updateAttributes();
	}

	private void updateAttributes() {
		if (isSupportColor())
			print( ansiAttributes(blink, bold) );
	}

	public static String ansiAttributes(boolean blink, boolean bold) {
		return (char)27 + "[0" + (blink ? ";5" : "") + (bold ? ";1" : "") + "m";
	}
	
	
	@Override
	public boolean isBlink() {
		return blink;
	}

	@Override
	public boolean isSupportBold() {
		return isSupportColor();
	}

	@Override
	public void setBold(boolean bold) {
		this.bold = bold;
		updateAttributes();
	}

	@Override
	public boolean isBold() {
		return bold;
	}
	
	
	public static String ansiColorValue(COLOR col) {
		switch (col) {
		case BLACK:
			return "0";
		case BLUE:
			return "4";
		case GREEN:
			return "2";
		case RED:
			return "1";
		case WHITE:
			return "7";
		case YELLOW:
			return "3";
		case CYAN:
			return "6";
		case MAGENTA:
			return "5";
		default:
			return "7";
		}
	}

	@Override
	public void cleanup() {
		bold = false;
		blink = false;
		foreground = COLOR.UNKNOWN;
		background = COLOR.UNKNOWN;
		print( ansiCleanup() );
	}
	
	public static String ansiCleanup() {
		return  (char)27 + "[0m";
	}

	@Override
	public void resetTerminal() {
		// \033c
//		print("\033[H\033[2J");  
		print( ansiReset());
	}

	public static String ansiReset() {
		return (char)27 + "c";
	}

	@Override
	public boolean isAnsi() {
		return true;
	}

	public static String[] getRawAnsiSettings() throws IOException {
		ConsoleReader reader = new ConsoleReader();
		return new String[] {
				"Width: " + reader.getTerminal().getWidth(),
				"Height: " + reader.getTerminal().getHeight(),
				"Ansi: " + reader.getTerminal().isAnsiSupported(),
				"Echo: " + reader.getTerminal().isEchoEnabled(),
				"Supported: " + reader.getTerminal().isSupported()
		};
	}
	
	@Override
	public void setWidth(int w) {
		this.width = w;
	}
	
	@Override
	public void setHeight(int h) {
		this.height = h;
	}

}
