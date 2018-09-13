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
import java.util.UUID;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MArgs;
import de.mhus.lib.core.cfg.CfgFile;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.system.IApi.SCOPE;

public class MVaultUtil {

	private static CfgFile defaultFile = new CfgFile(MVault.class, "file", MApi.getFile(SCOPE.ETC,"de.mhus.lib.core.vault.FileVaultSource.dat") );
	private static CfgFile defaultFolder = new CfgFile(MVault.class, "file", MApi.getFile(SCOPE.DATA,"de.mhus.lib.core.vault.FolderVaultSource") );
	
	public static MVault loadDefault() {
		MVault vault = MApi.lookup(MVault.class);
		checkDefault(vault);
		return vault;
	}
	
	public static void checkDefault(MVault vault) {
		VaultSource def = vault.getSource(MVault.SOURCE_DEFAULT);
		if (def == null) {
			
			VaultPassphrase vaultPassphrase = MApi.lookup(VaultPassphrase.class);
			
			if (defaultFile.value().exists()) {
				// legacy
				try {
					def = new FileVaultSource(defaultFile.value(), vaultPassphrase.getPassphrase(),MVault.SOURCE_DEFAULT);
					vault.registerSource(def);
				} catch (IOException e) {
					MLogUtil.log().d(e);
				}
			} else {
				// default
				try {
					def = new FolderVaultSource(defaultFolder.value(), vaultPassphrase.getPassphrase(),MVault.SOURCE_DEFAULT);
					vault.registerSource(def);
				} catch (IOException e) {
					MLogUtil.log().w(e);
				}
			}
		}
	}

	public static void main(String[] in) throws IOException {
		MArgs args = new MArgs(in);
		
		MVault vault = loadDefault();
		
		VaultSource source = null;
		if (args.contains("file")) {
			String vp = args.getValue("passphrase", "setit", 0);
			File f = new File(args.getValue("file", 0));
			source = new FileVaultSource(f, vp);
			vault.registerSource(source);
		}
		if (source == null) source = vault.getSource(MVault.SOURCE_DEFAULT);
		
		String cmd = args.getValue(MArgs.DEFAULT, "help", 0);
		
		switch (cmd) {
		case "help": {
			System.out.println("Usage: <cmd> <args>");
			System.out.println("list - list all keys");
		} break;
		case "list": {
			ConsoleTable out = new ConsoleTable();
			out.setHeaderValues("Source","Id","Type","Description");
			for (String sourceName : vault.getSourceNames()) {
				source = vault.getSource(sourceName);
				for (UUID id : source.getEntryIds()) {
					VaultEntry entry = source.getEntry(id);
					out.addRowValues(sourceName,id,entry.getType(),entry.getDescription());
				}
			}
			out.print(System.out);
		} break;
		}
		
	}

}
