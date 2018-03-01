package de.mhus.lib.core.security;

public class Ace {

	public static final String RIGHTS_NONE = "";
	public static final String RIGHTS_ALL = "crud";
	public static final String RIGHTS_RO = "r";
	public static final String RIGHTS_RU = "ru";
	public static final String GENERAL_OPERATOR = "operator";
	public static final Ace ACE_NONE = new Ace(RIGHTS_NONE);
	public static final Ace ACE_ALL = new Ace(RIGHTS_ALL);
	public static final Ace ACE_RO = new Ace(RIGHTS_RO);
	public static final Ace ACE_RU = new Ace(RIGHTS_RU);
	
	protected String rights;

	public Ace() {}
	
	public Ace(String rights) {
		this.rights = rights;
	}
	
	public boolean canCreate() {
		return hasFlag('c');
	}
	
	public boolean canRead() {
		return hasFlag('r');
	}
	
	public boolean canUpdate() {
		return hasFlag('u');
	}
	
	public boolean canDelete() {
		return hasFlag('d');
	}

	public boolean hasFlag(char f) {
		return rights != null && rights.indexOf(f) > -1;
	}

	public String getRights() {
		return rights;
	}
	
	@Override
	public String toString() {
		return rights;
	}

}
