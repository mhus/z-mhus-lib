package de.mhus.lib.portlet;

public class IllegalCharacterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalCharacterException(char c, String name) {
		super("illegal " + c + " character in name " + name);
	}

	
}
