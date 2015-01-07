package de.mhus.lib.test;

import java.util.UUID;

import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.persistence.aaa.DbAccess;
import de.mhus.lib.persistence.aaa.DbAccessObject;

@DbAccess(parentType=TestParent.class)
public class TestChild extends DbAccessObject {
	
	@DbPrimaryKey
	private UUID id;
	@DbPersistent
	private String name;
	@DbPersistent
	private UUID parentId;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this,name ,id);
	}

	public UUID getParentId() {
		return parentId;
	}

	public void setParentId(UUID parentId) {
		this.parentId = parentId;
	}

}
