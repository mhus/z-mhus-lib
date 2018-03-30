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
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.Subject;

public class MSecurity {

	public static final String USER_CLASS = "UserPrincipal";
	public static final String GROUP_CLASS = "GroupPrincipal";
	public static final String ROLE_CLASS = "RolePrincipal";

	public static Principal getUser(Subject subject) {
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(USER_CLASS)) return p;
		}
		return null;
	}
	
	public static boolean hasGroup(Subject subject, String name) {
		if (name == null) return false;
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(GROUP_CLASS) && name.equals(p.getName())) return true;
		}
		return false;
	}
	
	public static boolean hasRole(Subject subject, String name) {
		if (name == null) return false;
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(ROLE_CLASS) && name.equals(p.getName())) return true;
		}
		return false;
	}
	
	public static List<Principal> getGroups(Subject subject) {
		LinkedList<Principal> out = new LinkedList<Principal>();
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(GROUP_CLASS)) out.add(p);
		}
		return out;
	}
	
	public static List<Principal> getRoles(Subject subject) {
		LinkedList<Principal> out = new LinkedList<Principal>();
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(ROLE_CLASS)) out.add(p);
		}
		return out;
	}
	
}
