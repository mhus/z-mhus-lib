package de.mhus.lib.core.security;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.Subject;

/**
 * <p>MSecurity class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MSecurity {

	/** Constant <code>USER_CLASS="UserPrincipal"</code> */
	public static final String USER_CLASS = "UserPrincipal";
	/** Constant <code>GROUP_CLASS="GroupPrincipal"</code> */
	public static final String GROUP_CLASS = "GroupPrincipal";
	/** Constant <code>ROLE_CLASS="RolePrincipal"</code> */
	public static final String ROLE_CLASS = "RolePrincipal";

	/**
	 * <p>getUser.</p>
	 *
	 * @param subject a {@link javax.security.auth.Subject} object.
	 * @return a {@link java.security.Principal} object.
	 */
	public static Principal getUser(Subject subject) {
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(USER_CLASS)) return p;
		}
		return null;
	}
	
	/**
	 * <p>hasGroup.</p>
	 *
	 * @param subject a {@link javax.security.auth.Subject} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean hasGroup(Subject subject, String name) {
		if (name == null) return false;
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(GROUP_CLASS) && name.equals(p.getName())) return true;
		}
		return false;
	}
	
	/**
	 * <p>hasRole.</p>
	 *
	 * @param subject a {@link javax.security.auth.Subject} object.
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean hasRole(Subject subject, String name) {
		if (name == null) return false;
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(ROLE_CLASS) && name.equals(p.getName())) return true;
		}
		return false;
	}
	
	/**
	 * <p>getGroups.</p>
	 *
	 * @param subject a {@link javax.security.auth.Subject} object.
	 * @return a {@link java.util.List} object.
	 */
	public static List<Principal> getGroups(Subject subject) {
		LinkedList<Principal> out = new LinkedList<Principal>();
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(GROUP_CLASS)) out.add(p);
		}
		return out;
	}
	
	/**
	 * <p>getRoles.</p>
	 *
	 * @param subject a {@link javax.security.auth.Subject} object.
	 * @return a {@link java.util.List} object.
	 */
	public static List<Principal> getRoles(Subject subject) {
		LinkedList<Principal> out = new LinkedList<Principal>();
		for (Principal p : subject.getPrincipals()) {
			if ( p.getClass().getSimpleName().equals(ROLE_CLASS)) out.add(p);
		}
		return out;
	}
	
}
