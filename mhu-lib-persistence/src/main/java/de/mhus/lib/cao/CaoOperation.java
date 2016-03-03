package de.mhus.lib.cao;

/**
 * <p>Abstract CaoOperation class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoOperation {

	protected CaoMonitor monitor;
	protected CaoOperation nextOperation = null;

	/**
	 * <p>initialize.</p>
	 *
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public abstract void initialize() throws CaoException;

	/**
	 * <p>execute.</p>
	 *
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public abstract void execute() throws CaoException;

	/**
	 * <p>dispose.</p>
	 *
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public abstract void dispose() throws CaoException;

	/**
	 * <p>Setter for the field <code>monitor</code>.</p>
	 *
	 * @param monitor a {@link de.mhus.lib.cao.CaoMonitor} object.
	 */
	public void setMonitor(CaoMonitor monitor) {
		this.monitor=monitor;
	}

	/**
	 * <p>Getter for the field <code>nextOperation</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoOperation} object.
	 */
	public CaoOperation getNextOperation() {
		return nextOperation;
	}

}
