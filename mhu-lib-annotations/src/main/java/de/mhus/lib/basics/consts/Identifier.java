package de.mhus.lib.basics.consts;

/**
 * Represents a identifier
 * 
 * @author mikehummel
 *
 */
public class Identifier {

	private String id;
	private Class<?> clazz;

	public Identifier(String id) {
		this.id = id;
	}
	
	public Identifier(Class<?> clazz, String id) {
		this.id = id;
		this.clazz = clazz;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	public String getPojoName() {
		return id.toLowerCase();
	}
	
	@Override
	public boolean equals(Object other) {
		return id.equals(String.valueOf(other));
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
}
