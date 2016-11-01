package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoOperation;

public class AuthOperation extends CaoOperation {

	private CaoOperation instance;
	@SuppressWarnings("unused")
	private AuthConnection con;

	public AuthOperation(AuthConnection con, CaoOperation instance) {
		this.con = con;
		this.instance = instance;
	}

	@Override
	public void initialize() throws CaoException {
		instance.initialize();
	}

	@Override
	public void execute() throws CaoException {
		instance.execute();
	}

	@Override
	public void dispose() throws CaoException {
		instance.dispose();
	}

}
