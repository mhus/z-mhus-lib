package de.mhus.lib.core.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import de.mhus.lib.core.logging.Log.LEVEL;

public class StreamToLogAdapter extends PrintStream {

	private static Log log = Log.getLog("Console");

	public StreamToLogAdapter(LEVEL level, PrintStream forward) {
		super(new MyOutputStream(level, forward));
	}

	private static class MyOutputStream extends OutputStream {

		private LEVEL level;
		StringBuffer line = new StringBuffer();
		private PrintStream forward;
		
		public MyOutputStream(LEVEL level, PrintStream forward) {
			this.level = level;
			this.forward = forward; 
		}

		@Override
		public synchronized void write(int b) throws IOException {
			
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

		private void writeLine() {
			log.log(level, line);
			line.setLength(0);
		}
		
	}
}
