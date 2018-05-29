package de.mhus.lib.core.vault;

import de.mhus.lib.core.cfg.CfgString;

public class VaultPassphraseFromConfig implements VaultPassphrase {

	private static CfgString defaultPassphrase = new CfgString(MVault.class, "passphrase", "changeit" );

	@Override
	public String getPassphrase() {
		return defaultPassphrase.value();
	}

}
