package de.mhus.lib.sql;

import java.util.List;

import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoMetaDefinition;
import de.mhus.lib.cao.CaoMetadata;

/**
 * A mutable variant of the Metadata to rapid develop extensions.
 * 
 * @author mikehummel
 *
 */
public class MutableMetadata extends CaoMetadata {

	public MutableMetadata() {
		super(null); //TODO check !
	}
	
	public MutableMetadata(CaoDriver driver) {
		super(driver);
	}

	/**
	 * This method cleanup the internal index. Manipulate the map before you call a getter,
	 * this will recreate the internal index. Changes after it will not affect.
	 * 
	 * @return
	 */
	public List<CaoMetaDefinition> getMap() {
		synchronized (this) {
			index = null;
			return definition;
		}
	}

}
