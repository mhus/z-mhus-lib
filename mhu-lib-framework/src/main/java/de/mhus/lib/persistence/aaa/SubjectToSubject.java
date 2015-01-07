package de.mhus.lib.persistence.aaa;

import java.util.UUID;

import de.mhus.inka.constgenerator.GenerateConstFile;
import de.mhus.lib.adb.DbAccessManager;
import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.relation.RelSingle;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.annotations.adb.DbTable;

@DbTable(features=DbAccessManager.FEATURE_NAME)
@DbAccess(owner=SubjectToSubjectConst.PARENTID_lower, ownerType=Subject.class)
@GenerateConstFile
public class SubjectToSubject extends DbComfortableObject {

	@DbPrimaryKey
	private UUID id;

	@DbPersistent
	private UUID parentId;
	@DbPersistent
	private UUID childId;

	@DbRelation(target=Subject.class)
	private RelSingle<Subject> parent = new RelSingle<Subject>();
	@DbRelation(target=Subject.class)
	private RelSingle<Subject> child = new RelSingle<Subject>();

	public SubjectToSubject() {
	}
	
	public SubjectToSubject(Subject child) {
		this.child.setRelation(child);
	}

	public RelSingle<Subject> getParent() {
		return parent;
	}

	public RelSingle<Subject> getChild() {
		return child;
	}
	
}
