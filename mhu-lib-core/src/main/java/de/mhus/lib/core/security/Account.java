package de.mhus.lib.core.security;

public interface Account extends Rightful {
	
	String MAP_ADMIN = "admin";
	String ACT_READ = "read";
	String ACT_CREATE = "create";
	String ACT_UPDATE = "update";
	String ACT_MODIFY = "modify";
	String ACT_DELETE = "delete";

	boolean isValid();

	boolean validatePassword(String password);

	boolean isSyntetic();

	String getDisplayName();
	
}
