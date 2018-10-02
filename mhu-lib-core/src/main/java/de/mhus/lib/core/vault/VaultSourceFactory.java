package de.mhus.lib.core.vault;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(DefaultVaultSourceFactory.class)
public interface VaultSourceFactory {

	VaultSource create(String name, VaultPassphrase vaultPassphrase);

}
