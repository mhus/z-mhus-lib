package de.mhus.lib.core.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.core.io.TextReader;

public class SimpleConsole extends Console {

	private TextReader reader;
	private COLOR foreground;
	private COLOR background;
	private boolean blink;
	private boolean bold;
	
	public SimpleConsole() {
		super();
		reader = new TextReader(System.in);
	}

	public SimpleConsole(InputStream in, PrintStream out, boolean flush, String charset)
			throws UnsupportedEncodingException {
		super(out, flush, charset);
		reader = new TextReader(in);
	}

	public SimpleConsole(InputStream in, PrintStream out) {
		super(out);
		reader = new TextReader(in);
	}

	@Override
	public String readLine(LinkedList<String> history) {
		return reader.readLine();
//		return System.console().readLine();
	}

	@Override
	public boolean isSupportSize() {
		return false;
	}

	@Override
	public int getWidth() {
		return DEFAULT_WIDTH;
	}

	@Override
	public int getHeight() {
		return DEFAULT_HEIGHT;
	}

	@Override
	public boolean isSupportCursor() {
		return false;
	}

	@Override
	public void setCursor(int x, int y) {
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
		return false;
	}

	@Override
	public void setColor(COLOR foreground, COLOR background) {
		this.foreground = foreground;
		this.background = background;
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
		return false;
	}

	@Override
	public void setBlink(boolean blink) {
		this.blink = blink;
	}

	@Override
	public boolean isBlink() {
		return blink;
	}

	@Override
	public boolean isSupportBold() {
		return false;
	}

	@Override
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	@Override
	public boolean isBold() {
		return bold;
	}

	@Override
	public void cleanup() {
		bold = false;
		blink = false;
		foreground = COLOR.WHITE;
		background = COLOR.BLACK;
	}

}
