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

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.lang.IBase;

@DefaultImplementation(SimpleConsole.class)
public abstract class Console extends PrintStream implements IBase {

	public enum COLOR {UNKNOWN,WHITE,BLACK,RED,GREEN,BLUE,YELLOW,MAGENTA,CYAN};
	
	public static int DEFAULT_WIDTH = 40;
	public static int DEFAULT_HEIGHT = 25;
	
	public enum CONSOLE_TYPE {SIMPLE,ANSI,ANSI_COLOR,XTERM,XTERM_COLOR,CMD};
	private static CONSOLE_TYPE consoleType = null;
	private static Console console = null;
	
	public Console() {
		this(System.out);
	}

	public Console(PrintStream out) {
		super(out);
	}

	public Console(PrintStream out, boolean flush, String charset) throws UnsupportedEncodingException {
		super(out,flush,charset);
	}

	/**
	 * Factory to return the correct implementation of console.
	 * 
	 * @return a new console object
	 */
	public static Console create() {
		if (getConsoleType() == null) {
			String term = System.getenv("TERM");
			if (term != null) {
				term = term.toLowerCase();
				if (term.indexOf("xterm") >= 0) {
					if (term.indexOf("color") > 0)
						setConsoleType(CONSOLE_TYPE.XTERM_COLOR);
					else
						setConsoleType(CONSOLE_TYPE.XTERM);
				} else {
					setConsoleType(CONSOLE_TYPE.ANSI_COLOR);
				}
			} 
			if (getConsoleType() == null)
				setConsoleType(CONSOLE_TYPE.SIMPLE);
		}
		
		if (console == null) {
			console = create(getConsoleType());
		}
		return console;
	}
	
	public static Console create(CONSOLE_TYPE consoleType) {
		
		if (consoleType != null) {
			switch (consoleType) {
			case ANSI:
				return new ANSIConsole(false);
			case CMD:
				return new CmdConsole();
			case SIMPLE:
				return new SimpleConsole();
			case XTERM:
				return new XTermConsole(false);
			case ANSI_COLOR:
				return new ANSIConsole(true);
			case XTERM_COLOR:
				return new XTermConsole(true);
			default:
				return new SimpleConsole();
			}
		}
		
		if (MSystem.isWindows()) {
			return new CmdConsole();
		}
		String term = System.getenv("TERM");
		if (term != null) {
			term = term.toLowerCase();
			if (term.indexOf("xterm") >= 0) {
				return new XTermConsole(term.indexOf("color") > 0);
			}
			if (term.indexOf("ansi") >= 0)
				return new ANSIConsole(true);
		}
		return new SimpleConsole();
	}

//	public void initializeAsDefault() {
//		MApi.instance().setBaseDefault(Console.class,this);
//	}
	
	protected LinkedList<String> history = new LinkedList<String>();
	
	public String readLine() {
		return readLine(history );
	}
	
	
	public abstract String readLine(LinkedList<String> history);

	public char[] readPassword() {
		return System.console().readPassword();
	}
	
	public void cr() {
		print('\r');
	}

	public abstract boolean isSupportSize();
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
	public abstract boolean isSupportCursor();
	
	public abstract void setCursor(int x, int y);
	
	public abstract int getCursorX();
	
	public abstract int getCursorY();
	
	public ConsoleProgressBar createProgressBar() {
		return new ConsoleProgressBar(this);
	}
	
	public abstract boolean isSupportColor();
	
	public abstract void setColor(COLOR foreground, COLOR background);
	
	public abstract COLOR getForegroundColor();
	
	public abstract COLOR getBackgroundColor();
	
	public abstract boolean isSupportBlink();
	
	public abstract void setBlink(boolean blink);
	
	public abstract boolean isBlink();
	
	public abstract boolean isSupportBold();
	
	public abstract void setBold(boolean bold);
	
	public abstract boolean isBold();
	
	public abstract void cleanup();
	
	public void printLine() {
		printLine('-');
	}
	
	public void printLine(char c) {
		for (int i = 0; i < getWidth(); i++)
			print(c);
		println();
	}

	public void resetTerminal() {
	}

	public static CONSOLE_TYPE getConsoleType() {
		return consoleType;
	}

	public static void setConsoleType(CONSOLE_TYPE consoleType) {
		Console.consoleType = consoleType;
	}
	
	public static void resetConsole() {
		consoleType = null;
		console = null;
	}

	public static Console get() {
		if (console == null) create();
		return console;
	}
	
	public boolean isInitialized() {
		return console != null;
	}

	public boolean isAnsi() {
		return false;
	}
	
}
