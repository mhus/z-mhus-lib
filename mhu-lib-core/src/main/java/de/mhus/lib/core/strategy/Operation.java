package de.mhus.lib.core.strategy;

public interface Operation {

	void doExecute(TaskContext context) throws Exception;
	boolean isBusy();
	boolean setBusy(Object owner);
	boolean releaseBusy(Object owner);

}
