package de.mhus.lib.mongo;

import java.util.Date;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.basics.UuidIdentificable;

public class MoMetadata extends DbComfortableObject implements UuidIdentificable {

	@Id
	private ObjectId id;
	@Property
	private Date creationDate;
	@Property
	private Date modifyDate;
	@Property
	private long vstamp;

	@Override
	public UUID getId() {
		return MoUtil.toUUID(id);
	}
	
	public ObjectId getObjectId() {
		return id;
	}

	@PrePersist
	public void prePersist() {
		if (creationDate == null)
			creationDate = new Date();
		else
			vstamp += 1;
		modifyDate = new Date();
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public long getVstamp() {
		return vstamp;
	}
	
}
