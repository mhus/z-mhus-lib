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

import java.security.Principal;
import java.util.Date;
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
		reloadAccount();
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
	public boolean isSynthetic() {
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

	@Override
	public String[] getGroups() throws NotSupportedException {
		return groups.toArray(new String[groups.size()]);
	}

	@Override
	public boolean reloadAccount() {
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
		return true;
	}

	@Override
	public Date getCreationDate() {
		return null;
	}

	@Override
	public Date getModifyDate() {
		return null;
	}

}
