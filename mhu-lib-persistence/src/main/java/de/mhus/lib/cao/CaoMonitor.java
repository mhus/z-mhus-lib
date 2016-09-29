package de.mhus.lib.cao;

import de.mhus.lib.core.logging.Log;

/**
 * <p>Abstract CaoMonitor class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoMonitor {

	/** Constant indicating an unknown amount of work.
	 */
	public final static int UNKNOWN = -1;

	/**
	 * Notifies that the main task is beginning.  This must only be called once
	 * on a given progress monitor instance.
	 *
	 * @param name the name (or description) of the main task
	 * @param totalWork the total number of work units into which
	 *  the main task is been subdivided. If the value is <code>UNKNOWN</code>
	 *  the implementation is free to indicate progress in a way which
	 *  doesn't require the total number of work units in advance.
	 */
	public abstract void beginTask(String name, int totalWork);

	/**
	 * Returns whether cancelation of current operation has been requested.
	 * Long-running operations should poll to see if cancelation
	 * has been requested.
	 *
	 * @return <code>true</code> if cancellation has been requested,
	 *    and <code>false</code> otherwise
	 */
	public abstract boolean isCanceled();

	/**
	 * Notifies that a subtask of the main task is beginning.
	 * Subtasks are optional; the main task might not have subtasks.
	 *
	 * @param name the name (or description) of the subtask
	 */
	public abstract void subTask(String name);

	/**
	 * Notifies that a given number of work unit of the main task
	 * has been completed. Note that this amount represents an
	 * installment, as opposed to a cumulative amount of work done
	 * to date.
	 *
	 * @param work a non-negative number of work units just completed
	 */
	public abstract void worked(int work);

	/**
	 * Returns the actual number of already worked items set by worked().
	 *
	 * @return a int.
	 */
	public abstract int alreadyWorked();

	/**
	 * Notifies that the next work unit is finished. This call needs no
	 * knowledge of the actual worked items.
	 */
	public void nextFinished() {
		worked(alreadyWorked()+1);
	}

	/**
	 * Return a logger to use by the client. The client should use this
	 * logger to be sure the results of the operation will be stored
	 * in the correct and separate location.
	 *
	 * @return a {@link de.mhus.lib.core.logging.Log} object.
	 */
	public abstract Log log();
}
