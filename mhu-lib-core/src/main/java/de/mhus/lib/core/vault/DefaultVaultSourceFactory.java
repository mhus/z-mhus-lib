/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
