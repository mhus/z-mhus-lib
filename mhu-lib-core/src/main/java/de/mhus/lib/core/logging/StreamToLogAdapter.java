package de.mhus.lib.core.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import de.mhus.lib.core.logging.Log.LEVEL;

/**
 * <p>StreamToLogAdapter class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class StreamToLogAdapter extends PrintStream {

	/** Constant <code>log</code> */
	protected static Log log = Log.getLog("Console");

	protected LEVEL level;
	protected StringBuffer line = new StringBuffer();
	protected PrintStream forward;

	/**
	 * <p>Constructor for StreamToLogAdapter.</p>
	 *
	 * @param level a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
	 * @param forward a {@link java.io.PrintStream} object.
	 */
	public StreamToLogAdapter(LEVEL level, PrintStream forward) {
		super(new MyOutputStream());
		this.level = level;
		this.forward = forward;
		((MyOutputStream)out).setAdapter(this);
	}

	private static class MyOutputStream extends OutputStream {

		private StreamToLogAdapter adapter;
		
		public MyOutputStream() {
		}

		public void setAdapter(StreamToLogAdapter adapter) {
			this.adapter = adapter;
		}

		@Override
		public synchronized void write(int b) throws IOException {
			if (adapter == null) return;
			adapter.writeByte(b);
		}
		
	}

	/**
	 * <p>writeByte.</p>
	 *
	 * @param b a int.
	 */
	protected void writeByte(int b) {
		if (forward != null) 
			forward.write(b);
		
		if (b == '\n') {
			writeLine();
		} else
		if ( b == '\r') {
			// ignore characters
		} else
			line.append((char)b);
		if (line.length() > 1000) {
			writeLine();
		}
	}

	/**
	 * <p>writeLine.</p>
	 */
	protected void writeLine() {
		log.log(level, line);
		line.setLength(0);
	}

}
