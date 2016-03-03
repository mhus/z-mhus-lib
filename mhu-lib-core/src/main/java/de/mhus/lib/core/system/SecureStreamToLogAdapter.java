package de.mhus.lib.core.system;

import java.io.PrintStream;

import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.StreamToLogAdapter;

/**
 * <p>SecureStreamToLogAdapter class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class SecureStreamToLogAdapter extends StreamToLogAdapter {

	/** Constant <code>enter</code> */
	protected static ThreadLocal<Boolean> enter = new ThreadLocal<>();
	
	/**
	 * <p>Constructor for SecureStreamToLogAdapter.</p>
	 *
	 * @param level a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
	 * @param forward a {@link java.io.PrintStream} object.
	 */
	public SecureStreamToLogAdapter(LEVEL level, PrintStream forward) {
		super(level, forward);
	}

	/** {@inheritDoc} */
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
