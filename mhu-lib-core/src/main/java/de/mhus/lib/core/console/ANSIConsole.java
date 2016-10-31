package de.mhus.lib.core.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import de.mhus.lib.core.io.TextReader;

// http://ascii-table.com/ansi-escape-sequences.php

public class ANSIConsole extends Console {

	private TextReader reader;
	private COLOR foreground;
	private COLOR background;
	private boolean blink;
	private boolean bold;
	
	public ANSIConsole() {
		super();
		reader = new TextReader(System.in);
	}

	public ANSIConsole(InputStream in, PrintStream out, boolean flush, String charset)
			throws UnsupportedEncodingException {
		super(out, flush, charset);
		reader = new TextReader(in);
	}

	public ANSIConsole(InputStream in, PrintStream out) {
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
		return true;
	}

	@Override
	public void setCursor(int x, int y) {
		print( (char)27 + "[" + y + ";" + x + "H" );

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
		
		if (foreground != COLOR.UNKNOWN)
			print( (char)27 + "[3" + colorToSequence(foreground) + "m");
		if (background != COLOR.UNKNOWN)
			print( (char)27 + "[4" + colorToSequence(background) + "m");
		
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
		updateAttributes();
	}

	private void updateAttributes() {
		print( (char)27 + "[0" + (blink ? ";5" : "") + (bold ? ";1" : "") + "m" );
	}

	@Override
	public boolean isBlink() {
		return blink;
	}

	@Override
	public boolean isSupportBold() {
		return true;
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

	@Override
	public void cleanup() {
		bold = false;
		blink = false;
		foreground = COLOR.UNKNOWN;
		background = COLOR.UNKNOWN;
		print( (char)27 + "[0m");
	}

}
