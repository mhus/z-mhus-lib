package de.mhus.lib.core.security;

/**
 * <p>AccountSource interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface AccountSource {

	/**
	 * <p>findAccount.</p>
	 *
	 * @param account a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.security.Account} object.
	 */
	Account findAccount(String account);

}
