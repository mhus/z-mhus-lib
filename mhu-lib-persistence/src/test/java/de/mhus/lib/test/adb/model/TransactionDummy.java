package de.mhus.lib.test.adb.model;

import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.annotations.adb.DbPrimaryKey;

public class TransactionDummy extends DbComfortableObject {

	@DbPrimaryKey
	UUID id;
	
}
