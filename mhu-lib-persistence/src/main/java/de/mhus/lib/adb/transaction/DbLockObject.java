package de.mhus.lib.adb.transaction;


import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.sql.DbConnection;

public class DbLockObject extends DbComfortableObject {

	@DbPrimaryKey(auto_id=false)
	@DbPersistent(features="cut")
	private String key;
	
	@DbPersistent
	private long created;
	
	@DbPersistent
	private String owner;

	
	@Override
	public void doPreCreate(DbConnection con) {
		created = System.currentTimeMillis();
		super.doPreCreate(con);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getCreated() {
		return created;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public long getAge() {
		return System.currentTimeMillis() - created;
	}
	
}
