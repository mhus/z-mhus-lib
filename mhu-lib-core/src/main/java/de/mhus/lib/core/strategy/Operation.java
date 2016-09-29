package de.mhus.lib.core.strategy;

/**
 * <p>Operation interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Operation {

	/**
	 * <p>hasAccess.</p>
	 *
	 * @return a boolean.
	 */
	boolean hasAccess();
	/**
	 * <p>canExecute.</p>
	 *
	 * @param context a {@link de.mhus.lib.core.strategy.TaskContext} object.
	 * @return a boolean.
	 */
	boolean canExecute(TaskContext context);
	/**
	 * <p>getDescription.</p>
	 *
	 * @return a {@link de.mhus.lib.core.strategy.OperationDescription} object.
	 */
	OperationDescription getDescription();
	/**
	 * <p>doExecute.</p>
	 *
	 * @param context a {@link de.mhus.lib.core.strategy.TaskContext} object.
	 * @return a {@link de.mhus.lib.core.strategy.OperationResult} object.
	 * @throws java.lang.Exception if any.
	 */
	OperationResult doExecute(TaskContext context) throws Exception;
	/**
	 * <p>isBusy.</p>
	 *
	 * @return a boolean.
	 */
	boolean isBusy();
	/**
	 * <p>setBusy.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	boolean setBusy(Object owner);
	/**
	 * <p>releaseBusy.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	boolean releaseBusy(Object owner);

}
