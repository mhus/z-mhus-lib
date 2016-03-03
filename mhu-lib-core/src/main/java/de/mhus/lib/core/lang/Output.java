package de.mhus.lib.core.lang;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * <p>Output class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class Output extends PrintStream implements IBase {

	/**
	 * <p>Constructor for Output.</p>
	 *
	 * @throws java.io.FileNotFoundException if any.
	 */
	public Output() throws FileNotFoundException {
		super(System.out);
	}

}
