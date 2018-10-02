package de.mhus.lib.core.vault;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.errors.NotSupportedException;

@DefaultImplementation(DefaultVaultMutator.class)
public interface VaultMutator {

	/**
	 * Try to adapt the entry to the given class or interface.
	 * @param entry 
	 * @param ifc
	 * @return The requested interface or class.
	 * @throws NotSupportedException Thrown if the entry can't be adapted to the interface.
	 * @throws ParseException 
	 */
	<T> T adaptTo(VaultEntry entry, Class<? extends T> ifc) throws ParseException, NotSupportedException;

}
