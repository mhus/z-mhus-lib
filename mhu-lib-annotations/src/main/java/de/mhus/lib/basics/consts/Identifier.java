package de.mhus.lib.basics.consts;

/**
 * Represents a identifier
 * 
 * @author mikehummel
 *
 */
public class Identifier {

	private String id;

	public Identifier(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public boolean equals(Object other) {
		return id.equals(String.valueOf(other));
	}
	
}
