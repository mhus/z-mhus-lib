package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoOperation;
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
	public MForm createConfiguration(CaoList list, Object... initConfig) throws CaoException {
		return instance.createConfiguration(list, initConfig);
	}

	@Override
	public boolean canExecute(CaoList list, Object... initConfig) {
		return instance.canExecute(list, initConfig);
	}

	@Override
	public CaoOperation createOperation(CaoList list, Object configuration) throws CaoException {
		return new AuthOperation(con, instance.createOperation(list, configuration) );
	}

}
