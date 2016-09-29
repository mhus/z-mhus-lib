package de.mhus.lib.core.system;

import java.io.PrintStream;

import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.StreamToLogAdapter;

public class SecureStreamToLogAdapter extends StreamToLogAdapter {

	protected static ThreadLocal<Boolean> enter = new ThreadLocal<>();
	
	public SecureStreamToLogAdapter(LEVEL level, PrintStream forward) {
		super(level, forward);
	}

	@Override
	protected void writeLine() {
		if (enter.get() != null) return;
		enter.set(true);
		try {
			log.log(level, line);
			line.setLength(0);
		} finally {
			enter.remove();
		}
	}

}
