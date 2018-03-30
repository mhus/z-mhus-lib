/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import de.mhus.lib.core.logging.Log.LEVEL;

public class StreamToLogAdapter extends PrintStream {

	protected static Log log = Log.getLog("Console");

	protected LEVEL level;
	protected StringBuilder line = new StringBuilder();
	protected PrintStream forward;

	public StreamToLogAdapter(LEVEL level, PrintStream forward) {
		super(new MyOutputStream());
		this.level = level;
		this.forward = forward;
		((MyOutputStream)out).setAdapter(this);
	}

	@Override
	public void close() {
		if (line.length() > 0)
			writeLine();
		super.close();
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
