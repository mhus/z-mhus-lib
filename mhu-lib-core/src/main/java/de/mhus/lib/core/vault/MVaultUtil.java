package de.mhus.lib.core.vault;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MArgs;
import de.mhus.lib.core.cfg.CfgFile;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.logging.MLogUtil;

public class MVaultUtil {

	private static CfgFile defaultFile = new CfgFile(MVault.class, "file", MApi.getFile(".mhu-vault") );
	private static CfgString defaultPassphrase = new CfgString(MVault.class, "passphrase", "changeit" );
	
	public static MVault loadDefault() {
		MVault vault = MApi.lookup(MVault.class);
		VaultSource def = vault.getSource(MVault.SOURCE_DEFAULT);
		if (def == null) {
			try {
				def = new FileVaultSource(defaultFile.value(), defaultPassphrase.value(),MVault.SOURCE_DEFAULT);
				vault.registerSource(def);
			} catch (IOException e) {
				MLogUtil.log().d(e);
			}
		}
		return vault;
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
