package de.mhus.lib.vaadin;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.vaadin.ui.UI;

import de.mhus.lib.core.security.AccessControl;
import de.mhus.lib.core.security.LoginCallbackHandler;
import de.mhus.lib.core.security.MSecurity;

public class VaadinAccessControl implements AccessControl {

	public static final String SUBJECT_ATTR = "_subject";
	private String realm;

	public VaadinAccessControl() {}
	
	public VaadinAccessControl(String realm) {
		this.realm = realm;
	}
	
	@Override
	public boolean signIn(String username, String password) {
		LoginContext lc;
		try {
			LoginCallbackHandler handler = new LoginCallbackHandler(username,password);
			lc = new LoginContext(realm, handler);
			lc.login();
			UI.getCurrent().getSession().setAttribute(SUBJECT_ATTR, lc.getSubject());
			
			return true;
		} catch (LoginException e) {
			e.printStackTrace();
		}

		return false;

	}

	@Override
	public boolean isUserSignedIn() {
		return UI.getCurrent().getSession().getAttribute(SUBJECT_ATTR) != null;
	}

	@Override
	public boolean isUserInRole(String role) {
		Subject subject = (Subject)UI.getCurrent().getSession().getAttribute(SUBJECT_ATTR);
		if (subject == null) return false;
		
		return MSecurity.hasRole(subject, role);
	}

	@Override
	public String getPrincipalName() {
		Subject subject = (Subject)UI.getCurrent().getSession().getAttribute(SUBJECT_ATTR);
		if (subject == null) return "?";
		return MSecurity.getUser(subject).getName();
	}

	@Override
	public void signOut() {
		UI.getCurrent().getSession().setAttribute(SUBJECT_ATTR, null);
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

}
