package de.mhus.lib.core.vault;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(VaultPassphraseFromConfig.class)
public interface VaultPassphrase {

	public String getPassphrase();
	
}
