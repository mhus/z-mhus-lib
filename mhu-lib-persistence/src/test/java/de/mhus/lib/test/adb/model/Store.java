package de.mhus.lib.test.adb.model;

import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;

public class Store extends DbComfortableObject {

	private UUID id;
	private String name;
	private String address;
	private UUID principal;
	private java.sql.Date sqlDate;

	@DbPrimaryKey
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	@DbPersistent
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@DbPersistent
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@DbPersistent
	public UUID getPrincipal() {
		return principal;
	}
	public void setPrincipal(UUID principal) {
		this.principal = principal;
	}

	@DbPersistent
	public java.sql.Date getSqlDate() {
		return sqlDate;
	}
	public void setSqlDate(java.sql.Date sqlDate) {
		this.sqlDate = sqlDate;
	}



}
