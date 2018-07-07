package de.mhus.lib.core.security;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.MException;

public interface ModifyCurrentAccountApi {

	
	/**
	 * Return account of the 
	 * @return the account which will be modified with this api.
	 */
	Account getAccount();
	
	void changePassword(String newPassword) throws MException;
	
	void changeAccount(IReadProperties properties) throws MException;
	
}
