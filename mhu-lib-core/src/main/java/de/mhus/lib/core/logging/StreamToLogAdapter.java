package de.mhus.lib.core.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import de.mhus.lib.core.logging.Log.LEVEL;

public class StreamToLogAdapter extends PrintStream {

	protected static Log log = Log.getLog("Console");

	protected LEVEL level;
	protected StringBuffer line = new StringBuffer();
	protected PrintStream forward;

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

	protected void writeLine() {
		log.log(level, line);
		line.setLength(0);
	}

}
