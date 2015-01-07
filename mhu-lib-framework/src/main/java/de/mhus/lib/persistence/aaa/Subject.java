package de.mhus.lib.persistence.aaa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.mhus.inka.constgenerator.GenerateConstFile;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.relation.RelMultible;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.MException;

@GenerateConstFile
public class Subject extends DbAccessObject {

	public static enum TYPE {USER,GROUP,ROLE};
	
	@DbPrimaryKey
	private UUID id;
	@DbPersistent
	private String loginName;
	@DbPersistent
	private String displayName;
	@DbPersistent
	private Date lastLogin;
	@DbPersistent
	private Date expireDate;
	@DbPersistent
	private Date created;
	@DbPersistent
	private boolean active;
	@DbPersistent
	private String description;
	@DbPersistent
	private String secret;
	@DbPersistent
	private TYPE type;
		
	@DbRelation(target=SubjectToSubject.class,targetAttribute=SubjectToSubjectConst.PARENTID_lower)
	private RelMultible<SubjectToSubject> children = new RelMultible<SubjectToSubject>();
	
	@DbRelation(target=SubjectToSubject.class,targetAttribute=SubjectToSubjectConst.CHILDID_lower,managed=false)
	private RelMultible<SubjectToSubject> parents = new RelMultible<SubjectToSubject>();
	private List<Subject> affectedList;
	
	public Subject() {
	}
	
	public Subject(TYPE type, String loginName) {
		this.type = type;
		this.loginName = loginName;
		this.created = new Date();
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void doLogin(String secret) throws MException {
		if (!isAdbManaged()) throw new MException("subject not managed");
		if (expireDate != null && MDate.isEarlierAs(expireDate,new Date()))
			throw new MException("expired");
		if (this.secret == null || !this.secret.equals(secret)) throw new MException("login failed"); // check manager for a strategy
		this.lastLogin = new Date();
		save();
	}
	
	public void doExtendExpirationDate(String newSecret) throws MException {
		if (newSecret == null || newSecret.equals(secret)) throw new MException("wrong secret"); // maybe a problem
		if (expireDate == null) expireDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(expireDate);
		c.add(Calendar.MONTH, 1);
		secret = newSecret;
		expireDate = c.getTime();
		save();
	}

	
	
	public UUID getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public Date getCreated() {
		return created;
	}

	public boolean isActive() {
		return active;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public TYPE getType() {
		return type;
	}

	public RelMultible<SubjectToSubject> getChildren() {
		return children;
	}

	public RelMultible<SubjectToSubject> getParents() {
		return parents;
	}
	
	public boolean equals(Object obj) {
		if (id == null) return false;
		if (obj instanceof Subject) {
			return id.equals(((Subject)obj).getId());
		}
		return id.equals(obj);
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, type, loginName,id);
	}

	public synchronized List<Subject> getAffectedSubjects(DbManager manager) throws Exception {
		if (affectedList == null) {
			affectedList = AaaUtil.findAffectedSubjects(manager, this);
		}
		return affectedList;
	}
	
}
