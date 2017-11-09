package de.mhus.lib.core.vault;

import java.util.UUID;

import de.mhus.lib.errors.NotSupportedException;

public interface VaultSource {

	/**
	 * Return a entry by id or null if not found.
	 * 
	 * @param id
	 * @return The id or null
	 */
	VaultEntry getEntry(UUID id);

	/**
	 * Return a list of current stored entry ids.
	 * 
	 * @return a list of ids.
	 */
	UUID[] getEntryIds();
	
	/**
	 * Try to adapt the source to the given class or interface.
	 * @param ifc
	 * @return The requested interface or class.
	 * @throws NotSupportedException Thrown if the source can't be adapted to the interface.
	 */
	<T> T adaptTo(Class<? extends T> ifc) throws NotSupportedException;

	/**
	 * Return a unique name of the source.
	 * 
	 * @return the name
	 */
	String getName();
	
}
