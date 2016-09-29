package de.mhus.lib.core.security;

/**
 * <p>Account interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public interface Account {
	
	/** Constant <code>MAP_ADMIN="admin"</code> */
	String MAP_ADMIN = "admin";
	/** Constant <code>ACT_READ="read"</code> */
	String ACT_READ = "read";
	/** Constant <code>ACT_CREATE="create"</code> */
	String ACT_CREATE = "create";
	/** Constant <code>ACT_UPDATE="update"</code> */
	String ACT_UPDATE = "update";
	/** Constant <code>ACT_MODIFY="modify"</code> */
	String ACT_MODIFY = "modify";
	/** Constant <code>ACT_DELETE="delete"</code> */
	String ACT_DELETE = "delete";

	/**
	 * <p>getAccount.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getAccount();

	/**
	 * <p>isValide.</p>
	 *
	 * @return a boolean.
	 */
	boolean isValide();

	/**
	 * <p>validatePassword.</p>
	 *
	 * @param password a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	boolean validatePassword(String password);

	/**
	 * <p>isSyntetic.</p>
	 *
	 * @return a boolean.
	 */
	boolean isSyntetic();

	/**
	 * <p>getDisplayName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getDisplayName();

	/**
	 * <p>hasGroup.</p>
	 *
	 * @param group a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	boolean hasGroup(String group);
	
}
