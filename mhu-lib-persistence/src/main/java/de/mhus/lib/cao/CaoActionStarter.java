package de.mhus.lib.cao;

import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.util.MNls;

public class CaoActionStarter {

	private CaoAction action;
	private CaoConfiguration config;

	public CaoActionStarter(CaoAction action, CaoConfiguration config) {
		this.action = action;
		this.config = config;
	}
	
	public String getName() {
		return action.getName();
	}
	public boolean canExecute() {
		return action.canExecute(config);
	}
	
	public OperationResult doExecute(Monitor monitor) throws CaoException {
		if (monitor == null)
			monitor = action.checkMonitor(monitor);
		return action.doExecute(config, monitor);
	}
	
	public MNls getResourceBundle() {
		return action.getResourceBundle();
	}

}
