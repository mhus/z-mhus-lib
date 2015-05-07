package de.mhus.lib.core.strategy;

public interface Operation {

	boolean canExecute(TaskContext context);
	OperationDescription getDescription();
	OperationResult doExecute(TaskContext context) throws Exception;
	boolean isBusy();
	boolean setBusy(Object owner);
	boolean releaseBusy(Object owner);

}
