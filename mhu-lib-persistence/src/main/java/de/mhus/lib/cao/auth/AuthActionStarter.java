package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoActionStarter;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.util.MNls;

public class AuthActionStarter extends CaoActionStarter {

	private CaoActionStarter starter;

	public AuthActionStarter(CaoActionStarter starter) {
		super(null, null);
		this.starter = starter;
	}

	@Override
	public String getName() {
		return starter.getName();
	}

	@Override
	public boolean canExecute() {
		return starter.canExecute();
	}

	@Override
	public OperationResult doExecute(Monitor monitor) throws CaoException {
		return starter.doExecute(monitor);
	}

	@Override
	public MNls getResourceBundle() {
		return starter.getResourceBundle();
	}

	@Override
	public boolean equals(Object obj) {
		return starter.equals(obj);
	}

	@Override
	public String toString() {
		return starter.toString();
	}
	
}
