package de.mhus.lib.core.strategy;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.logging.Log;

/**
 * <p>DefaultMonitor class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultMonitor extends MLog implements Monitor {

	private long steps;
	private long step;
	private StringBuffer lineBuffer = new StringBuffer();

	/**
	 * <p>Getter for the field <code>steps</code>.</p>
	 *
	 * @return a long.
	 * @since 3.2.9
	 */
	public long getSteps() {
		return steps;
	}

	/** {@inheritDoc} */
	@Override
	public void setSteps(long steps) {
		this.steps = steps;
	}

	/**
	 * <p>Getter for the field <code>step</code>.</p>
	 *
	 * @return a long.
	 * @since 3.2.9
	 */
	public long getStep() {
		return step;
	}

	/** {@inheritDoc} */
	@Override
	public void setStep(long step) {
		this.step = step;
	}

	/** {@inheritDoc} */
	@Override
	public Log log() {
		return super.log();
	}

	/** {@inheritDoc} */
	@Override
	public void println() {
		synchronized (lineBuffer) {
			log().i("STDOUT", lineBuffer);
			lineBuffer = new StringBuffer();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void println(Object... out) {
		print(out);
		println();
	}

	/** {@inheritDoc} */
	@Override
	public void print(Object... out) {
		synchronized (lineBuffer) {
			for (Object o : out)
				lineBuffer.append(o);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void incrementStep() {
		step++;
	}

}
