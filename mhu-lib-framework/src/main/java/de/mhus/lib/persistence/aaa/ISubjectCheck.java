package de.mhus.lib.persistence.aaa;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.sql.DbConnection;

public interface ISubjectCheck {

	boolean hasRight(DbManager manager, DbConnection con, Acl acl,
			String rightWrite);

	boolean isAdministrator(DbManager manager, DbConnection con);

	String getCurrentUserInfo();

}
