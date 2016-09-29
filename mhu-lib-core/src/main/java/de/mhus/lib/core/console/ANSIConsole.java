package de.mhus.lib.core.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.omg.CORBA.UNKNOWN;

import de.mhus.lib.core.console.Console.COLOR;
import de.mhus.lib.core.io.TextReader;

// http://ascii-table.com/ansi-escape-sequences.php

/**
 * <p>ANSIConsole class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ANSIConsole extends Console {

	private TextReader reader;
	private COLOR foreground;
	private COLOR background;
	private boolean blink;
	private boolean bold;
	
	/**
	 * <p>Constructor for ANSIConsole.</p>
	 */
	public ANSIConsole() {
		super();
		reader = new TextReader(System.in);
	}

	/**
	 * <p>Constructor for ANSIConsole.</p>
	 *
	 * @param in a {@link java.io.InputStream} object.
	 * @param out a {@link java.io.PrintStream} object.
	 * @param flush a boolean.
	 * @param charset a {@link java.lang.String} object.
	 * @throws java.io.UnsupportedEncodingException if any.
	 */
	public ANSIConsole(InputStream in, PrintStream out, boolean flush, String charset)
			throws UnsupportedEncodingException {
		super(out, flush, charset);
		reader = new TextReader(in);
	}

	/**
	 * <p>Constructor for ANSIConsole.</p>
	 *
	 * @param in a {@link java.io.InputStream} object.
	 * @param out a {@link java.io.PrintStream} object.
	 */
	public ANSIConsole(InputStream in, PrintStream out) {
		super(out);
		reader = new TextReader(in);
	}

	/** {@inheritDoc} */
	@Override
	public String readLine(LinkedList<String> history) {
		return reader.readLine();
//		return System.console().readLine();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupportSize() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int getWidth() {
		return DEFAULT_WIDTH;
	}

	/** {@inheritDoc} */
	@Override
	public int getHeight() {
		return DEFAULT_HEIGHT;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupportCursor() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void setCursor(int x, int y) {
		print( (char)27 + "[" + y + ";" + x + "H" );

	}

	/** {@inheritDoc} */
	@Override
	public int getCursorX() {
		return -1;
	}

	/** {@inheritDoc} */
	@Override
	public int getCursorY() {
		return -1;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupportColor() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setColor(COLOR foreground, COLOR background) {
		this.foreground = foreground;
		this.background = background;
		
		if (foreground != COLOR.UNKNOWN)
			print( (char)27 + "[3" + colorToSequence(foreground) + "m");
		if (background != COLOR.UNKNOWN)
			print( (char)27 + "[4" + colorToSequence(background) + "m");
		
	}

	/** {@inheritDoc} */
	@Override
	public COLOR getForegroundColor() {
		return foreground;
	}

	/** {@inheritDoc} */
	@Override
	public COLOR getBackgroundColor() {
		return background;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupportBlink() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setBlink(boolean blink) {
		this.blink = blink;
		updateAttributes();
	}

	private void updateAttributes() {
		print( (char)27 + "[0" + (blink ? ";5" : "") + (bold ? ";1" : "") + "m" );
	}

	/** {@inheritDoc} */
	@Override
	public boolean isBlink() {
		return blink;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupportBold() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void setBold(boolean bold) {
		this.bold = bold;
		updateAttributes();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isBold() {
		return bold;
	}
	
	
	/**
	 * <p>colorToSequence.</p>
	 *
	 * @param col a {@link de.mhus.lib.core.console.Console.COLOR} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String colorToSequence(COLOR col) {
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

	/** {@inheritDoc} */
	@Override
	public void cleanup() {
		bold = false;
		blink = false;
		foreground = COLOR.UNKNOWN;
		background = COLOR.UNKNOWN;
		print( (char)27 + "[0m");
	}

}
