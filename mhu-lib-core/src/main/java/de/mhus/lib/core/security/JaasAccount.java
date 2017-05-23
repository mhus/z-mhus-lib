package de.mhus.lib.core.security;

import java.security.Principal;
import java.util.HashSet;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.NotSupportedException;

public class JaasAccount implements Account {

	private String realm;
	private Subject subject;
	private String userName;
	private HashSet<String> groups;
	private MProperties attr;

	public JaasAccount(String realm, Subject subject) {
		this.realm = realm;
		this.subject = subject;
		
		{  // find user name
			groups = new HashSet<>();
			attr = new MProperties();
			int cnt = 0;
			for (Principal principal : subject.getPrincipals()) {
				switch (principal.getClass().getSimpleName()) {
				case "UserPrincipal":
					userName = principal.getName();
					break;
				case "RolePrincipal":
					groups.add(principal.getName());
					break;
				}
				attr.put(principal.getClass().getSimpleName() + "." + cnt, principal.getName());
				cnt++;
			}
		}
	}

	@Override
	public boolean hasGroup(String role) {
		return groups.contains(role);
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public boolean isValid() {
		return subject != null;
	}

	@Override
	public boolean validatePassword(String password) {
		LoginContext lc;
		try {
			LoginCallbackHandler handler = new LoginCallbackHandler(userName,password);
			lc = new LoginContext(realm, handler);
			lc.login();
			
			return true;
		} catch (LoginException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean isSyntetic() {
		return false;
	}

	@Override
	public String getDisplayName() {
		return userName;
	}

	@Override
	public IReadProperties getAttributes() {
		return attr;
	}

	@Override
	public void putAttributes(IReadProperties properties) throws NotSupportedException {
		throw new NotSupportedException();
	}

}
