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
import de.mhus.lib.core.logging.MLogUtil;
import jline.Terminal;
import jline.TerminalFactory;

@DefaultImplementation(SimpleConsole.class)
public abstract class Console extends PrintStream implements IBase {

	public enum COLOR {UNKNOWN,WHITE,BLACK,RED,GREEN,BLUE,YELLOW,MAGENTA,CYAN};
	
	public static int DEFAULT_WIDTH = 40;
	public static int DEFAULT_HEIGHT = 25;
	
	public enum CONSOLE_TYPE {SIMPLE,ANSI,XTERM,CMD};
	private static ThreadLocal<CONSOLE_TYPE> consoleTypes = new ThreadLocal<>();
	private static ThreadLocal<Console> consoles = new ThreadLocal<>();
	
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
			if (MSystem.isWindows()) {
				setConsoleType(CONSOLE_TYPE.CMD);
			} else {
				Terminal terminal = TerminalFactory.get();
				if (terminal.isAnsiSupported()) {
					setConsoleType(CONSOLE_TYPE.ANSI);
				} else {
					setConsoleType(CONSOLE_TYPE.XTERM);
				}
			}
		}
		
		Console console = consoles.get();
		if (console == null) {
			console = create(getConsoleType());
			consoles.set(console);
		}
		return console;
	}
	
	public static Console create(CONSOLE_TYPE consoleType) {
		
		try {
			if (consoleType != null) {
				switch (consoleType) {
				case ANSI:
					return new ANSIConsole();
				case CMD:
					return new CmdConsole();
				case SIMPLE:
					return new SimpleConsole();
				case XTERM:
					return new XTermConsole();
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
					return new XTermConsole();
				}
				if (term.indexOf("ansi") >= 0)
					return new ANSIConsole();
			}
		} catch (Exception e) {
			MLogUtil.log().t(e);
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
		return consoleTypes.get();
	}

	public static void setConsoleType(CONSOLE_TYPE consoleType) {
		consoleTypes.set(consoleType);
	}
	
	public static void resetConsole() {
		consoleTypes.remove();
		consoles.remove();
	}
	
	public synchronized static Console get() {
		Console console = consoles.get();
		if (console == null) {
			create();
			console = consoles.get();
		}
		return console;
	}
	
	public boolean isInitialized() {
		Console console = consoles.get();
		return console != null;
	}

	public boolean isAnsi() {
		return false;
	}
	
}
