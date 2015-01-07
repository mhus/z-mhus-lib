package de.mhus.lib.persistence.aaa;

import java.util.UUID;

import de.mhus.inka.constgenerator.GenerateConstFile;
import de.mhus.lib.adb.relation.RelMultible;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.core.MSystem;

@GenerateConstFile
public class Acl extends DbAccessObject {

	public static final String RIGHT_WRITE = "write";

	public static final String RIGHT_REMOVE = "remove";

	public static final String RIGHT_READ = "read";

	public static final String RIGHT_ALL = "all";

	public static final String RIGHT_CREATE = "create";

	public static final String RIGHT_MOVE = "move";
	
	@DbPrimaryKey
	private UUID id;
	@DbPersistent
	private String name;
	@DbPersistent
	private String description;
	@DbPersistent
	private String defaultPolicy;
	
	@DbRelation(target=AclToSubject.class,targetAttribute=AclToSubjectConst.ACLID_lower,orderBy=AclToSubjectConst.SORT_lower)
	private RelMultible<AclToSubject> rules = new RelMultible<AclToSubject>();

	public RelMultible<AclToSubject> getRules() {
		return rules;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultPolicy() {
		return defaultPolicy;
	}

	public void setDefaultPolicy(String defaultPolicy) {
		this.defaultPolicy = defaultPolicy;
	}

	public UUID getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (id == null) return false;
		if (obj instanceof Acl) {
			return id.equals(((Acl)obj).getId());
		}
		return id.equals(obj);
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, name,id);
	}

}
