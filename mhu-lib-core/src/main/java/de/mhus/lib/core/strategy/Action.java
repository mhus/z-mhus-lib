package de.mhus.lib.core.strategy;

/**
 * <p>Action interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Action {

	/**
	 * <p>canExecute.</p>
	 *
	 * @param context a {@link de.mhus.lib.core.strategy.TaskContext} object.
	 * @return a boolean.
	 */
	boolean canExecute(TaskContext context);
	/**
	 * <p>createOperation.</p>
	 *
	 * @param context a {@link de.mhus.lib.core.strategy.TaskContext} object.
	 * @return a {@link de.mhus.lib.core.strategy.Operation} object.
	 */
	Operation createOperation(TaskContext context);
	// createConfig() ...
}
