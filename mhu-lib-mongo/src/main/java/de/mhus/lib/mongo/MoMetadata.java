/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.mongo;

import java.util.Date;
import java.util.UUID;

import org.bson.types.ObjectId;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.basics.UuidIdentificable;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;
import dev.morphia.annotations.Property;

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
