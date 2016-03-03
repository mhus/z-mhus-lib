package de.mhus.lib.core.strategy;

/**
 * <p>Abstract ExecuteStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class ExecuteStrategy extends AbstractOperation {

	/** {@inheritDoc} */
	@Override
	protected abstract OperationResult doExecute2(TaskContext context) throws Exception;
	
	/**
	 * <p>getExecutable.</p>
	 *
	 * @return a {@link de.mhus.lib.core.strategy.Operation} object.
	 */
	public abstract Operation getExecutable();
	/**
	 * <p>setExecutable.</p>
	 *
	 * @param executable a {@link de.mhus.lib.core.strategy.Operation} object.
	 */
	public abstract void setExecutable(Operation executable);
	
	
}
