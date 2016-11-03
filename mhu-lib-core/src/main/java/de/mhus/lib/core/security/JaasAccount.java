package de.mhus.lib.core.security;

import javax.security.auth.Subject;

public class JaasAccount implements Account {

	public JaasAccount(String realm, Subject subject) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hasGroup(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validatePassword(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSyntetic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

}
