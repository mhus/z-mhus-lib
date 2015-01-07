package de.mhus.lib.test;

import java.util.UUID;

import de.mhus.inka.constgenerator.GenerateConstFile;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.persistence.aaa.DbAccessObject;

//@DbTable(features=DbAccessManager.FEATURE_NAME)
@GenerateConstFile
public class TestMe extends DbAccessObject {
	
	@DbPrimaryKey
	private UUID id;
	
	@DbPersistent
	private String name;
	
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
	public boolean equals(Object obj) {
		if (id == null) return false;
		if (obj instanceof TestMe) {
			return id.equals(((TestMe)obj).getId());
		}
		return id.equals(obj);
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this,name ,id);
	}

}