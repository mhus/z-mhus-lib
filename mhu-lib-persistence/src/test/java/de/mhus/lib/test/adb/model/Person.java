package de.mhus.lib.test.adb.model;

import java.util.UUID;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.relation.RelMultible;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbRelation;


public class Person implements Persistable {

	private UUID id;
	private String name;
	private RelMultible<Book> lendTo = new RelMultible<Book>();
	
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
	
	@DbRelation(target=Book.class)
	public RelMultible<Book> getLendTo() {
		return lendTo;
	}

	
	
	
}
