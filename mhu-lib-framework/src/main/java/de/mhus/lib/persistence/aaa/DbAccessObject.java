package de.mhus.lib.persistence.aaa;

import java.util.UUID;

import de.mhus.lib.adb.DbAccessManager;
import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.annotations.adb.DbTable;

@DbTable(features=DbAccessManager.FEATURE_NAME)
public class DbAccessObject extends DbComfortableObject {

	@DbPersistent
	private UUID acl;
	@DbRelation
	private RelAcl aclOriginal = new RelAcl();

	public UUID getAcl() {
		return acl;
	}

	public void setAcl(UUID acl) {
		this.acl = acl;
	}

}
