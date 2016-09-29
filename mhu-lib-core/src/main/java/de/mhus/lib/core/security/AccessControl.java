package de.mhus.lib.core.security;

public interface AccessControl {

    public void signOut();
    
    public boolean signIn(String username, String password);

    public boolean isUserSignedIn();

    public boolean isUserInRole(String role);

    public String getPrincipalName();
}
