package de.mhus.lib.core.strategy;

public interface Action {

	boolean canExecute(TaskContext context);
	Operation createOperation(TaskContext context);
	// createConfig() ...
}
