package de.mhus.lib.core.security;

/**
 * <p>AuthorizationSource interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface AuthorizationSource {
	
	/**
	 * Return null if the mapping was not found, true or false if there is a concrete result.
	 *
	 * @param account a {@link de.mhus.lib.core.security.Account} object.
	 * @param mappingName a {@link java.lang.String} object.
	 * @param id a {@link java.lang.String} object.
	 * @param action a {@link java.lang.String} object.
	 * @return a {@link java.lang.Boolean} object.
	 */
	Boolean hasResourceAccess(Account account, String mappingName, String id, String action);
}
