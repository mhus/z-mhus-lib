package de.mhus.lib.core.lang;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Output extends PrintStream implements IBase {

	public Output() throws FileNotFoundException {
		super(System.out);
	}

}
