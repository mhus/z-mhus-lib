package de.mhus.lib.core.security;

public interface AccessControl extends Rightful {

	boolean signIn(String username, String password);

	boolean isUserSignedIn();

	void signOut();

	Account getAccount();

}
