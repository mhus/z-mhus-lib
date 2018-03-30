/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.security;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import de.mhus.lib.core.IProperties;

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
