package de.mhus.lib.core.console;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.MLogUtil;

public class DefaultConsoleFactory implements ConsoleFactory {

	@Override
	public Console create() {
		try {
			if (MSystem.isWindows()) {
				return new CmdConsole();
			}
			String term = System.getenv("TERM");
			if (term != null) {
				term = term.toLowerCase();
				if (term.indexOf("xterm") >= 0) {
					return new XTermConsole();
				}
				if (term.indexOf("ansi") >= 0)
					return new ANSIConsole();
			}
		} catch (Throwable t) {
			MLogUtil.log().d(t);
		}
		return new SimpleConsole();
	}

}
