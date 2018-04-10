package de.mhus.lib.core.security;

import java.util.Collection;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.MException;

public interface ModifyAccountApi {

	void createAccount(String username, String password, IReadProperties properties) throws MException;
	
	void deleteAccount(String username) throws MException;
	
	void changePassword(String username, String newPassword) throws MException;
	
	void changeAccount(String username, IReadProperties properties) throws MException;
	
	void appendGroups(String username, String ... group) throws MException;
	
	void removeGroups(String username, String ... group) throws MException;

	Collection<String> getGroups(String username) throws MException;
	
}
