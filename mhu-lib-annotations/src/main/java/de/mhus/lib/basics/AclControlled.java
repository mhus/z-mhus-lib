package de.mhus.lib.basics;

public interface AclControlled {
	public static final String ACL_ALL_ALL = "*=crud";
	public static final String ACL_ALL_RO = "*=r";
	public static final String ACL_ALL_RU = "*=ru";
	public static final String ACL_ALL_NONE = "*=";
	
	public String getAcl();
	
}
