package de.mhus.lib.core.security;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.security.AccessControl;
import de.mhus.lib.core.security.Account;
import de.mhus.lib.core.security.JaasAccount;
import de.mhus.lib.core.security.LoginCallbackHandler;
import de.mhus.lib.core.security.MSecurity;

public class JaasAccessControl implements AccessControl {

	public static final String SUBJECT_ATTR = "_subject";
	private String realm;
	private Account account;
	private IProperties session;
	
	public JaasAccessControl(String realm, IProperties session) {
		this.realm = realm;
		this.session = session;
	}
	
	@Override
	public boolean signIn(String username, String password) {
		LoginContext lc;
		try {
			LoginCallbackHandler handler = new LoginCallbackHandler(username,password);
			lc = new LoginContext(realm, handler);
			lc.login();
			session.put(SUBJECT_ATTR, lc.getSubject());
			
			return true;
		} catch (LoginException e) {
			e.printStackTrace();
		}

		return false;

	}

	@Override
	public boolean isUserSignedIn() {
		return session.get(SUBJECT_ATTR) != null;
	}

	@Override
	public boolean hasGroup(String role) {
		Subject subject = (Subject)session.get(SUBJECT_ATTR);
		if (subject == null) return false;
		
		return MSecurity.hasRole(subject, role);
	}

	@Override
	public String getName() {
		Subject subject = (Subject)session.get(SUBJECT_ATTR);
		if (subject == null) return "?";
		return MSecurity.getUser(subject).getName();
	}

	@Override
	public void signOut() {
		session.remove(SUBJECT_ATTR);
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	@Override
	public synchronized Account getAccount() {
		if (account == null) {
			Subject subject = (Subject)session.get(SUBJECT_ATTR);
			account = new JaasAccount(realm, subject);
		}
		return account;
	}

}
