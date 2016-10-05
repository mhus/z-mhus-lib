package de.mhus.lib.core.security;

public interface Account {
	
	String MAP_ADMIN = "admin";
	String ACT_READ = "read";
	String ACT_CREATE = "create";
	String ACT_UPDATE = "update";
	String ACT_MODIFY = "modify";
	String ACT_DELETE = "delete";

	String getAccount();

	boolean isValide();

	boolean validatePassword(String password);

	boolean isSyntetic();

	String getDisplayName();

	boolean hasGroup(String group);
	
}
