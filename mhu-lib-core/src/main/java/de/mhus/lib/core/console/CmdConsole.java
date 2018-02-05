package de.mhus.lib.core.console;

import java.io.IOException;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;

public class CmdConsole extends SimpleConsole {

	public CmdConsole() {
		super();
	}

	@Override
	public void resetTerminal() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (Exception e) {
		}
	}
/*
Status for device CON:
----------------------
Lines: 300
Columns: 80
Keyboard rate: 31
Keyboard delay: 1
Code page: 437
 */
	public void loadSettings() {
		
		try {
			String[] res = MSystem.execute("cmd.exe", "/c", "mode con");
			String[] parts = res[0].split("\n");
			for (String p : parts) {
				if (p.startsWith("Lines:"))
					height = MCast.toint(p.substring(7),DEFAULT_HEIGHT);
				else
				if (p.startsWith("Columns:"))
					width = MCast.toint(p.substring(9), DEFAULT_WIDTH);
			}
		} catch (IOException e) {
		}
	}

}
