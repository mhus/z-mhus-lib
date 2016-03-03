package de.mhus.lib.core.console;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.IBase;

/**
 * <p>Abstract Console class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@DefaultImplementation(SimpleConsole.class)
public abstract class Console extends PrintStream implements IBase {

	public enum COLOR {UNKNOWN,WHITE,BLACK,RED,GREEN,BLUE,YELLOW,MAGENTA,CYAN};
	
	/** Constant <code>DEFAULT_WIDTH=40</code> */
	public static final int DEFAULT_WIDTH = 40;
	/** Constant <code>DEFAULT_HEIGHT=25</code> */
	public static final int DEFAULT_HEIGHT = 25;
	
	
	/**
	 * <p>Constructor for Console.</p>
	 */
	public Console() {
		this(System.out);
	}

	/**
	 * <p>Constructor for Console.</p>
	 *
	 * @param out a {@link java.io.PrintStream} object.
	 */
	public Console(PrintStream out) {
		super(out);
	}

	/**
	 * <p>Constructor for Console.</p>
	 *
	 * @param out a {@link java.io.PrintStream} object.
	 * @param flush a boolean.
	 * @param charset a {@link java.lang.String} object.
	 * @throws java.io.UnsupportedEncodingException if any.
	 */
	public Console(PrintStream out, boolean flush, String charset) throws UnsupportedEncodingException {
		super(out,flush,charset);
	}

	/**
	 * Factory to return the correct implementation of console.
	 *
	 * @return a {@link de.mhus.lib.core.console.Console} object.
	 */
	public static Console create() {
		return new SimpleConsole();
	}

//	public void initializeAsDefault() {
//		MSingleton.instance().setBaseDefault(Console.class,this);
//	}
	
	protected LinkedList<String> history = new LinkedList<String>();
	
	/**
	 * <p>readLine.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String readLine() {
		return readLine(history );
	}
	
	
	/**
	 * <p>readLine.</p>
	 *
	 * @param history a {@link java.util.LinkedList} object.
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String readLine(LinkedList<String> history);

	/**
	 * <p>readPassword.</p>
	 *
	 * @return an array of char.
	 */
	public char[] readPassword() {
		return System.console().readPassword();
	}
	
	/**
	 * <p>cr.</p>
	 */
	public void cr() {
		print('\r');
	}

	/**
	 * <p>isSupportSize.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isSupportSize();
	
	/**
	 * <p>getWidth.</p>
	 *
	 * @return a int.
	 */
	public abstract int getWidth();
	
	/**
	 * <p>getHeight.</p>
	 *
	 * @return a int.
	 */
	public abstract int getHeight();
	
	/**
	 * <p>isSupportCursor.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isSupportCursor();
	
	/**
	 * <p>setCursor.</p>
	 *
	 * @param x a int.
	 * @param y a int.
	 */
	public abstract void setCursor(int x, int y);
	
	/**
	 * <p>getCursorX.</p>
	 *
	 * @return a int.
	 */
	public abstract int getCursorX();
	
	/**
	 * <p>getCursorY.</p>
	 *
	 * @return a int.
	 */
	public abstract int getCursorY();
	
	/**
	 * <p>createProgressBar.</p>
	 *
	 * @return a {@link de.mhus.lib.core.console.ConsoleProgressBar} object.
	 */
	public ConsoleProgressBar createProgressBar() {
		return new ConsoleProgressBar(this);
	}
	
	/**
	 * <p>isSupportColor.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isSupportColor();
	
	/**
	 * <p>setColor.</p>
	 *
	 * @param foreground a {@link de.mhus.lib.core.console.Console.COLOR} object.
	 * @param background a {@link de.mhus.lib.core.console.Console.COLOR} object.
	 */
	public abstract void setColor(COLOR foreground, COLOR background);
	
	/**
	 * <p>getForegroundColor.</p>
	 *
	 * @return a {@link de.mhus.lib.core.console.Console.COLOR} object.
	 */
	public abstract COLOR getForegroundColor();
	
	/**
	 * <p>getBackgroundColor.</p>
	 *
	 * @return a {@link de.mhus.lib.core.console.Console.COLOR} object.
	 */
	public abstract COLOR getBackgroundColor();
	
	/**
	 * <p>isSupportBlink.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isSupportBlink();
	
	/**
	 * <p>setBlink.</p>
	 *
	 * @param blink a boolean.
	 */
	public abstract void setBlink(boolean blink);
	
	/**
	 * <p>isBlink.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isBlink();
	
	/**
	 * <p>isSupportBold.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isSupportBold();
	
	/**
	 * <p>setBold.</p>
	 *
	 * @param bold a boolean.
	 */
	public abstract void setBold(boolean bold);
	
	/**
	 * <p>isBold.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isBold();
	
	/**
	 * <p>cleanup.</p>
	 */
	public abstract void cleanup();
	
	/**
	 * <p>printLine.</p>
	 */
	public void printLine() {
		printLine('-');
	}
	
	/**
	 * <p>printLine.</p>
	 *
	 * @param c a char.
	 */
	public void printLine(char c) {
		for (int i = 0; i < getWidth(); i++)
			print(c);
		println();
	}
	
}
