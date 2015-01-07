package de.mhus.lib.persistence.aaa;

import java.util.UUID;

import de.mhus.inka.constgenerator.GenerateConstFile;
import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.relation.RelSingle;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbRelation;

@GenerateConstFile
@DbAccess(owner=AclToSubjectConst.ACLID_lower, ownerType=Acl.class)
public class AclToSubject extends DbComfortableObject {

	@DbPrimaryKey
	private UUID id;

	@DbPersistent
	private UUID aclId;
	@DbPersistent
	private UUID subjectId;
	@DbPersistent
	private String policy;
	@DbPersistent
	private int sort;

	@DbRelation(target=Acl.class)
	private RelSingle<Acl> acl = new RelSingle<Acl>();
	@DbRelation(target=Subject.class)
	private RelSingle<Subject> subject = new RelSingle<Subject>();
	
	public AclToSubject() {
	}
	
	public AclToSubject(String policy, Subject subject) {
		this.policy = policy;
		this.subject.setRelation(subject);
	}
	
	public AclToSubject(String policy,Acl acl) {
		this.policy = policy;
		this.acl.setRelation(acl);
	}
	
	public AclToSubject(String policy,Subject subject, Acl acl) {
		this.policy = policy;
		this.subject.setRelation(subject);
		this.acl.setRelation(acl);
	}
	
	public RelSingle<Acl> getAcl() {
		return acl;
	}

	public RelSingle<Subject> getSubject() {
		return subject;
	}

	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public UUID getSubjectId() {
		return subjectId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
