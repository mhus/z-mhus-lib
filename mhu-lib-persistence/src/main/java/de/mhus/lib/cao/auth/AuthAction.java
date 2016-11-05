package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoOperation;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.form.MForm;

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
	public MForm createConfiguration(CaoList list, IProperties configuration) throws CaoException {
		return instance.createConfiguration(list, configuration);
	}

	@Override
	public boolean canExecute(CaoList list, IProperties configuration) {
		return instance.canExecute(list, configuration);
	}

	@Override
	public CaoOperation createOperation(CaoList list, IProperties configuration) throws CaoException {
		return new AuthOperation(con, instance.createOperation(list, configuration) );
	}

}
