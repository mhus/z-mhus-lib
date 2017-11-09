package de.mhus.lib.core.vault;

import java.io.IOException;
import java.util.UUID;

import de.mhus.lib.errors.NotSupportedException;

public interface VaultEntry {

	/**
	 * Returns the unique id of the entry.
	 * @return The unique id
	 */
	UUID getId();
	
	/**
	 * Returns the type of the entry as string. A list of default
	 * types is defined in MVault.
	 * 
	 * @return The type of the entry, never null.
	 */
	String getType();
	
	/**
	 * Return a readable description describe the key and/or the usage.
	 * @return
	 */
	String getDescription();
	
	/**
	 * Return the value of the entry as text.
	 * 
	 * @return The entry as text.
	 */
	String getValue();
	
	/**
	 * Try to adapt the entry to the given class or interface.
	 * @param ifc
	 * @return The requested interface or class.
	 * @throws NotSupportedException Thrown if the entry can't be adapted to the interface.
	 * @throws IOException 
	 */
	<T> T adaptTo(Class<? extends T> ifc) throws NotSupportedException, IOException;
}
