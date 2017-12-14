package de.mhus.lib.mongo;

import java.util.LinkedList;
import java.util.List;

import org.mongodb.morphia.mapping.Mapper;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.transaction.LockStrategy;

public abstract class MoSchema {

	private LinkedList<Class<? extends Persistable>> objectTypes;
	protected LockStrategy lockStrategy; // set this object to enable locking
	
	public abstract void findObjectTypes(List<Class<? extends Persistable>> list);

	public void initMapper(Mapper mapper) {
		
	}

	public abstract String getDatabaseName();
	
	
}
