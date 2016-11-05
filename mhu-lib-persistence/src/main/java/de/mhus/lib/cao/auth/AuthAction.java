package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.OperationResult;

public class AuthAction extends CaoAction {

	protected CaoAction instance;
	private AuthConnection con;

	public AuthAction(AuthConnection con, CaoAction instance) {
		this.con = con;
		this.instance = instance;
	}

	@Override
	public String getName() {
		return instance.getName();
	}

	@Override
	public CaoConfiguration createConfiguration(CaoList list, IProperties configuration) throws CaoException {
		return instance.createConfiguration(list, configuration);
	}

	@Override
	public boolean canExecute(CaoConfiguration configuration) {
		return instance.canExecute(configuration);
	}

	@Override
	public OperationResult doExecute(CaoConfiguration configuration, Monitor monitor) throws CaoException {
		return instance.doExecute(configuration, monitor);
	}

}
