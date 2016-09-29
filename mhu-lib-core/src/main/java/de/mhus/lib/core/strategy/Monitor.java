package de.mhus.lib.core.strategy;

import de.mhus.lib.core.logging.Log;

/**
 * <p>Monitor interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Monitor {

	/**
	 * <p>println.</p>
	 */
	void println();
	/**
	 * <p>println.</p>
	 *
	 * @param out a {@link java.lang.Object} object.
	 */
	void println(Object...out);
	/**
	 * <p>print.</p>
	 *
	 * @param out a {@link java.lang.Object} object.
	 */
	void print(Object...out);
	
	/**
	 * <p>log.</p>
	 *
	 * @return a {@link de.mhus.lib.core.logging.Log} object.
	 */
	Log log();

	/**
	 * <p>setSteps.</p>
	 *
	 * @param steps a long.
	 */
	void setSteps(long steps);
	/**
	 * <p>setStep.</p>
	 *
	 * @param step a long.
	 */
	void setStep(long step);
	/**
	 * <p>incrementStep.</p>
	 */
	void incrementStep();
		
}
