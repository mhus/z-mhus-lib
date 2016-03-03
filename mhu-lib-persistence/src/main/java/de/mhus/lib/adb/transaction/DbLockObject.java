package de.mhus.lib.adb.transaction;


import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.sql.DbConnection;

/**
 * <p>DbLockObject class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class DbLockObject extends DbComfortableObject {

	@DbPrimaryKey(auto_id=false)
	@DbPersistent(features="cut")
	private String key;
	
	@DbPersistent
	private long created;
	
	@DbPersistent
	private String owner;

	
	/** {@inheritDoc} */
	@Override
	public void doPreCreate(DbConnection con) {
		created = System.currentTimeMillis();
		super.doPreCreate(con);
	}

	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <p>Setter for the field <code>key</code>.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * <p>Getter for the field <code>created</code>.</p>
	 *
	 * @return a long.
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * <p>Getter for the field <code>owner</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * <p>Setter for the field <code>owner</code>.</p>
	 *
	 * @param owner a {@link java.lang.String} object.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * <p>getAge.</p>
	 *
	 * @return a long.
	 */
	public long getAge() {
		return System.currentTimeMillis() - created;
	}
	
}
