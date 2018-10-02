package de.mhus.lib.core.vault;

import java.io.File;
import java.io.IOException;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.cfg.CfgFile;
import de.mhus.lib.core.system.IApi.SCOPE;

public class DefaultVaultSourceFactory extends MLog implements VaultSourceFactory {

	private static CfgFile defaultFile = new CfgFile(MVault.class, "file", MApi.getFile(SCOPE.ETC,"de.mhus.lib.core.vault.FileVaultSource.dat") );
	private static CfgFile defaultFolder = new CfgFile(MVault.class, "file", MApi.getFile(SCOPE.DATA,"de.mhus.lib.core.vault.FolderVaultSource") );

	@Override
	public VaultSource create(String name, VaultPassphrase vaultPassphrase) {
		VaultSource def = null;
		if (MVault.SOURCE_DEFAULT.equals(name)) {
			if (defaultFile.value().exists()) {
				// legacy
				try {
					def = new FileVaultSource(defaultFile.value(), vaultPassphrase.getPassphrase(),name);
				} catch (IOException e) {
					log().d(e);
				}
			} else {
				// default
				try {
					def = new FolderVaultSource(defaultFolder.value(), vaultPassphrase.getPassphrase(),name);
				} catch (IOException e) {
					log().w(e);
				}
			}
		} else {
			File file = new File(name);
			if (file.exists() && file.isFile()) {
				try {
					def = new FileVaultSource(file, vaultPassphrase.getPassphrase(),file.getName());
				} catch (IOException e) {
					log().d(e);
				}
			} else {
				try {
					def = new FolderVaultSource(file, vaultPassphrase.getPassphrase(),file.getName());
				} catch (IOException e) {
					log().w(e);
				}
			}
		}
		return def;
	}

}
