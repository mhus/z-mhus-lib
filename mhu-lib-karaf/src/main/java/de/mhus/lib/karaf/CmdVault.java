package de.mhus.lib.karaf;

import java.util.UUID;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.vault.DefaultEntry;
import de.mhus.lib.core.vault.MVault;
import de.mhus.lib.core.vault.MVaultTool;
import de.mhus.lib.core.vault.MutableVaultSource;
import de.mhus.lib.core.vault.VaultEntry;
import de.mhus.lib.core.vault.VaultSource;

@Command(scope = "mhus", name = "vault", description = "Vault Manipulation")
@Service
public class CmdVault extends MLog implements Action {

	@Argument(index=0, name="cmd", required=true, description="Command:\n sources,list", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

	@Option(name="-s", aliases="--source",description="Set vault source name")
	String sourcename = MVault.SOURCE_DEFAULT;
	
	@Override
	public Object execute() throws Exception {
		MVault vault = MVaultTool.loadDefault();
		
		if (cmd.equals("sources")) {
			ConsoleTable out = new ConsoleTable();
			out.setHeaderValues("Source","Info");
			for (String sourceName : vault.getSourceNames()) {
				VaultSource source = vault.getSource(sourceName);
				out.addRowValues(sourceName,source);
			}
			out.print(System.out);
		} else
		if (cmd.equals("list")) {
			ConsoleTable out = new ConsoleTable();
			out.setHeaderValues("Source","Id","Type","Description");
			for (String sourceName : vault.getSourceNames()) {
				VaultSource source = vault.getSource(sourceName);
				for (UUID id : source.getEntryIds()) {
					VaultEntry entry = source.getEntry(id);
					out.addRowValues(sourceName,id,entry.getType(),entry.getDescription());
				}
			}
			out.print(System.out);
		} else
		if (cmd.equals("addraw")) {
			VaultSource source = vault.getSource(sourcename);
			if (source == null) {
				System.out.println("Source not found!");
				return null;
			}
			MutableVaultSource mutable = source.adaptTo(MutableVaultSource.class);
			
			String type = parameters[0];
			String description = parameters[1];
			String value = parameters[2];
			DefaultEntry entry = new DefaultEntry(type, description, value);
			mutable.addEntry(entry);
			System.out.println("OK");
		} else
		if (cmd.equals("save")) {
			VaultSource source = vault.getSource(sourcename);
			if (source == null) {
				System.out.println("Source not found!");
				return null;
			}
			MutableVaultSource mutable = source.adaptTo(MutableVaultSource.class);
			mutable.doSave();
			System.out.println("OK");
		} else
		if (cmd.equals("load")) {
			VaultSource source = vault.getSource(sourcename);
			if (source == null) {
				System.out.println("Source not found!");
				return null;
			}
			MutableVaultSource mutable = source.adaptTo(MutableVaultSource.class);
			mutable.doLoad();
			System.out.println("OK");
		}
		
		return null;
	}

}
