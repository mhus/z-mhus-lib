package de.mhus.lib.core.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.core.io.TextReader;

/**
 * <p>SimpleConsole class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SimpleConsole extends Console {

	private TextReader reader;
	private COLOR foreground;
	private COLOR background;
	private boolean blink;
	private boolean bold;
	
	/**
	 * <p>Constructor for SimpleConsole.</p>
	 */
	public SimpleConsole() {
		super();
		reader = new TextReader(System.in);
	}

	/**
	 * <p>Constructor for SimpleConsole.</p>
	 *
	 * @param in a {@link java.io.InputStream} object.
	 * @param out a {@link java.io.PrintStream} object.
	 * @param flush a boolean.
	 * @param charset a {@link java.lang.String} object.
	 * @throws java.io.UnsupportedEncodingException if any.
	 */
	public SimpleConsole(InputStream in, PrintStream out, boolean flush, String charset)
			throws UnsupportedEncodingException {
		super(out, flush, charset);
		reader = new TextReader(in);
	}

	/**
	 * <p>Constructor for SimpleConsole.</p>
	 *
	 * @param in a {@link java.io.InputStream} object.
	 * @param out a {@link java.io.PrintStream} object.
	 */
	public SimpleConsole(InputStream in, PrintStream out) {
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
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setCursor(int x, int y) {
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
	}

	/** {@inheritDoc} */
	@Override
	public boolean isBlink() {
		return blink;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSupportBold() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isBold() {
		return bold;
	}

	/** {@inheritDoc} */
	@Override
	public void cleanup() {
		bold = false;
		blink = false;
		foreground = COLOR.WHITE;
		background = COLOR.BLACK;
	}

}
