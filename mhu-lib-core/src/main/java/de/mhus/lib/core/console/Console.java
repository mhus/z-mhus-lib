package de.mhus.lib.core.console;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.IBase;

@DefaultImplementation(SimpleConsole.class)
public abstract class Console extends PrintStream implements IBase {

	public enum COLOR {UNKNOWN,WHITE,BLACK,RED,GREEN,BLUE,YELLOW,MAGENTA,CYAN};
	
	public static final int DEFAULT_WIDTH = 40;
	public static final int DEFAULT_HEIGHT = 25;
	
	
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
	 * @return
	 */
	public static Console create() {
		return new SimpleConsole();
	}

//	public void initializeAsDefault() {
//		MSingleton.instance().setBaseDefault(Console.class,this);
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
	
}
