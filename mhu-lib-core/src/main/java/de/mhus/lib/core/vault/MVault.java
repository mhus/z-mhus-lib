package de.mhus.lib.core.vault;

import java.util.UUID;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(DefaultVault.class)
public interface MVault {

	static final String TYPE_RSA_PRIVATE_KEY = "rsa.cipher.private.key";
	static final String TYPE_RSA_PUBLIC_KEY = "rsa.cipher.public.key";
	static final String TYPE_DSA_PRIVATE_KEY = "dsa.sign.private.key";
	static final String TYPE_DSA_PUBLIC_KEY = "dsa.sign.public.key";
	static final String TYPE_ECC_PRIVATE_KEY = "ecc.sign.private.key";
	static final String TYPE_ECC_PUBLIC_KEY = "ecc.sign.public.key";
	
	static final String TYPE_TEXT = "text";
	static final String SOURCE_DEFAULT = "default";
	
	/**
	 * Register a new source for VaultEntries
	 * 
	 * @param source
	 */
	void registerSource(VaultSource source);
	/**
	 * Unregister a registered source
	 * 
	 * @param sourceName
	 */
	void unregisterSource(String sourceName);
	
	/**
	 * Return a list of registered sources
	 * @return a list of names.
	 */
	String[] getSourceNames();
	
	/**
	 * Return a single source or null if not found.
	 * 
	 * @param name
	 * @return the source or null.
	 */
	VaultSource getSource(String name);
	
	/**
	 * Return a entry by id or null if not found.
	 * 
	 * @param id
	 * @return The entry or null.
	 */
	VaultEntry getEntry(UUID id);
	
	
}
