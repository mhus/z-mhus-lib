package de.mhus.lib.core.strategy;

import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.core.util.Nls;

public interface Operation extends MNlsProvider, Nls{

	boolean hasAccess();
	boolean canExecute(TaskContext context);
	OperationDescription getDescription();
	OperationResult doExecute(TaskContext context) throws Exception;
	boolean isBusy();
	boolean setBusy(Object owner);
	boolean releaseBusy(Object owner);

}
