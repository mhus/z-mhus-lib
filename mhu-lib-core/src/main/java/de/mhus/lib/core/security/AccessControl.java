package de.mhus.lib.core.security;

/**
 * <p>AccessControl interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface AccessControl {

    /**
     * <p>signOut.</p>
     */
    public void signOut();
    
    /**
     * <p>signIn.</p>
     *
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean signIn(String username, String password);

    /**
     * <p>isUserSignedIn.</p>
     *
     * @return a boolean.
     */
    public boolean isUserSignedIn();

    /**
     * <p>isUserInRole.</p>
     *
     * @param role a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isUserInRole(String role);

    /**
     * <p>getPrincipalName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPrincipalName();
}
