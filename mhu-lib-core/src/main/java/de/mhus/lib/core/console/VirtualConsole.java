package de.mhus.lib.core.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import de.mhus.lib.core.io.PipedStream;
import de.mhus.lib.core.io.TextReader;

public class VirtualConsole extends Console {

	protected int height = DEFAULT_HEIGHT;
	protected int width = DEFAULT_WIDTH;
	private Props[][] buffer = null;
	private int x;
	private int y;
	private COLOR foreground;
	private COLOR background;
	private boolean blink;
	private boolean bold;
	protected boolean echo;

	
//	private PipedInputStream inPipe = new PipedInputStream();
//	private PipedOutputStream outPipe = new PipedOutputStream();
//	private TextReader reader = new TextReader(inPipe);
	
	private PipedStream piped = new PipedStream();
	private TextReader reader = new TextReader(piped.getIn());

	private VirtualInStream writerStream = new VirtualInStream();
	private PrintStream writer = new PrintStream(writerStream);
	
	private boolean quiet;
	
	public VirtualConsole() throws IOException {
		super( new PrintStream(new ByteArrayOutputStream()),true,"ASCII");
		out = new VirtualOutStream();
		//inPipe.connect(outPipe);
		reset();
	}
	
//	public void initializeAsDefault() {
//		super.initializeAsDefault();
////		System.setIn(inPipe);
//		System.setIn(piped.getIn());
//		//TODO switch System.setOut(this);
//		//System.setErr(this);
//	}
	
	public void reset() {
		blink = false;
		bold = false;
		background = COLOR.BLACK;
		foreground = COLOR.WHITE;
		x = 0;
		y = 0;
		buffer = new Props[height][width];
		for (int y=0; y < height; y++)
			for (int x=0; x < width; x++)
				buffer[y][x] = new Props();
	}

	@Override
	public String readLine(LinkedList<String> history) {
		return reader.readLine();
	}

	@Override
	public char[] readPassword() {
		quiet = true;
		try {
			String ret = readLine();
			return ret.toCharArray();
		} finally {
			quiet = false;
		}
	}
	
	@Override
	public boolean isSupportSize() {
		return true;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	public void resize(int width, int height) {
		Props[][] newBuffer = new Props[height][width];
		for (int y=0; y < height; y++)
			for (int x=0; x < width; x++)
				newBuffer[y][x] = new Props();

		for (int y=0; y < this.height; y++)
			for (int x=0; x < this.width; x++)
				if (y < height && x < width)
					newBuffer[y][x] = buffer[y][x];
		
		buffer = newBuffer;
		this.width = width;
		this.height = height;
		x = x % width;
		y = Math.min(y, height-1);
		
	}

	@Override
	public boolean isSupportCursor() {
		return true;
	}

	@Override
	public void setCursor(int x, int y) {
		this.x = x % width;
		this.y = Math.min(y, height-1);
	}

	@Override
	public int getCursorX() {
		return x;
	}

	@Override
	public int getCursorY() {
		return y;
	}

	@Override
	public boolean isSupportColor() {
		return true;
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
		return true;
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
		return true;
	}

	@Override
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	@Override
	public boolean isBold() {
		return bold;
	}
	
	public PrintStream getInputWriter() {
		return writer;
	}

	public String getMonoDisplayAsString() {
		
		StringBuffer out = new StringBuffer();
		
		out.append("+");
		for (int x = 0; x < width; x++)
			out.append("-");
		out.append("+\n");
		
		for (int y = 0; y < height; y++) {
			out.append("|");
			for (int x = 0; x < width; x++)
				out.append(buffer[y][x].c);
			out.append("|\n");
		}

		out.append("+");
		for (int x = 0; x < width; x++)
			out.append("-");
		out.append("+");
		
		return out.toString();
	}

	
	protected class Props {
		COLOR bg = background;
		COLOR fg = foreground;
		boolean blink = VirtualConsole.this.blink;
		boolean bold = VirtualConsole.this.bold;
		char c = ' ';
		
		void set(char c) {
			this.c = c;
			bg = background;
			fg = foreground;
			blink = VirtualConsole.this.blink;
			bold = VirtualConsole.this.bold;
		}
	}
	
	protected void fillInputBuffer(char c) throws IOException {
		if (!quiet) writeChar(c);
//		outPipe.write(c);
		piped.getOut().write(c);
	}
	
	protected void writeChar(char c) {
		
		if (echo)
			System.out.print(c);
		
		// for secure
		x = x % width;
		y = Math.min(y, height-1);
		
		if (c == '\r') {
			x = 0;
		} else
		if (c == '\t') {
			while (x % 7 == 0) 
				writeChar(' ');	
		} else
		if (c == '\n') {
			x = 0;
			if (y >= height-1) {
				scrollLineUp();
			} else {
				y++;
			}
		} else {
		
			Props p = buffer[y][x];
			if (p == null) {
				// fore secure
				p = new Props();
				buffer[y][x] = p;
			}
			
			p.set(c);
			
			// move cursor
			if (x >= width-1) {
				if (y >= height-1) {
					scrollLineUp();
				} else {
					y++;
				}
				x = 0;
			} else {
				x++;
			}
		}
		
		// for secure
		x = x % width;
		y = Math.min(y, height-1);
		
	}
	
	private void scrollLineUp() {
		// swap buffer
		for (int i = 0; i < height-1; i++)
			buffer[i] = buffer[i+1];
//			for (int j = 0; j < WIDTH; j++)
//				buffer[i][j] = buffer[i+1][j];
		
		// new line
		buffer[height-1] = new Props[width];
		for (int i = 0; i < width; i++)
			buffer[height-1][i] = new Props();
	}


	private class VirtualOutStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			writeChar((char)b);
		}
		
	}
	
	private class VirtualInStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			fillInputBuffer((char)b);
		}
		
	}
	
	@Override
	public void cleanup() {
		bold = false;
		blink = false;
		foreground = COLOR.WHITE;
		background = COLOR.BLACK;
	}

}
