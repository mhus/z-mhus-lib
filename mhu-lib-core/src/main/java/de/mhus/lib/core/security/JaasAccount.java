package de.mhus.lib.core.security;

import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

	public JaasAccount(String realm, Subject subject) {
		this.realm = realm;
		this.subject = subject;
		
		{  // find user name
			Set<UserPrincipal> principals = subject.getPrincipals(UserPrincipal.class);
			Iterator<UserPrincipal> iter = principals.iterator();
			if (iter.hasNext()) {
				Principal next = iter.next();
				userName = next.getName();
			}
		}
		
		{ // find groups
			groups = new HashSet<>();
			for (GroupPrincipal principal : subject.getPrincipals(GroupPrincipal.class)) {
				groups.add(principal.getName());
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
		MProperties a = new MProperties();
		int cnt = 0;
		for (Principal principal : subject.getPrincipals()) {
			a.put(principal.getClass().getSimpleName() + "." + cnt, principal.getName());
			cnt++;
		}
		return a;
	}

	@Override
	public void putAttributes(IReadProperties properties) throws NotSupportedException {
		throw new NotSupportedException();
	}

}
