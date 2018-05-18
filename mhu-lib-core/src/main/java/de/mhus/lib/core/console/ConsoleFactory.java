package de.mhus.lib.core.console;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(DefaultConsoleFactory.class)
public interface ConsoleFactory {

	public Console create();
	
}
