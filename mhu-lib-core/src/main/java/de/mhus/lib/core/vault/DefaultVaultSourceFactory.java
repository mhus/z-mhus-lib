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
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgFile;

public class DefaultVaultSourceFactory extends MLog implements VaultSourceFactory {

	private static CfgFile CFG_DEFAULT_FILE = new CfgFile(MVault.class, "file", MApi.getFile(MApi.SCOPE.ETC,"de.mhus.lib.core.vault.FileVaultSource.dat") );
	private static CfgFile CFG_DEFAULT_FOLDER = new CfgFile(MVault.class, "folder", MApi.getFile(MApi.SCOPE.DATA,"de.mhus.lib.core.vault.FolderVaultSource") );
    private static CfgBoolean CFG_EDITABLE = new CfgBoolean(VaultSourceFromPlainProperties.class, "editable", false);

	@Override
	public VaultSource create(String name, VaultPassphrase vaultPassphrase) {
		VaultSource def = null;
		if (MVault.SOURCE_DEFAULT.equals(name)) {
			if (CFG_DEFAULT_FILE.value().exists()) {
			    
                if (CFG_DEFAULT_FILE.value().getName().endsWith(".json")) {
                    try {
                        def = new VaultSourceFromPlainJson(CFG_DEFAULT_FILE.value(), CFG_EDITABLE.value(), name);
                    } catch (IOException e) {
                        log().d(e);
                    }
                } else 
			    if (CFG_DEFAULT_FILE.value().getName().endsWith(".properties")) {
                    try {
                        def = new VaultSourceFromPlainProperties(CFG_DEFAULT_FILE.value(), CFG_EDITABLE.value(), name);
                    } catch (IOException e) {
                        log().d(e);
                    }
			    } else {
    				// legacy
    				try {
    					def = new VaultSourceFromSecFile(CFG_DEFAULT_FILE.value(), vaultPassphrase.getPassphrase(),name);
    				} catch (IOException e) {
    					log().d(e);
    				}
			    }
			} else {
				// default
				try {
					def = new VaultSourceFromSecFolder(CFG_DEFAULT_FOLDER.value(), vaultPassphrase.getPassphrase(),name);
				} catch (IOException e) {
					log().w(e);
				}
			}
		} else {
			File file = new File(name);
			if (file.exists() && file.isFile()) {
				try {
					def = new VaultSourceFromSecFile(file, vaultPassphrase.getPassphrase(),file.getName());
				} catch (IOException e) {
					log().d(e);
				}
			} else {
				try {
					def = new VaultSourceFromSecFolder(file, vaultPassphrase.getPassphrase(),file.getName());
				} catch (IOException e) {
					log().w(e);
				}
			}
		}
		return def;
	}

}
