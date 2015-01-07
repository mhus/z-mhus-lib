package de.mhus.lib.core.strategy;

public abstract class ExecuteStrategy extends AbstractOperation {

	@Override
	public abstract void doExecute(TaskContext context) throws Exception;
	
	public abstract Operation getExecutable();
	public abstract void setExecutable(Operation executable);
	
	
}
