package de.mhus.lib.sql;

import java.util.List;

import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoMetaDefinition;
import de.mhus.lib.cao.CaoMetadata;

/**
 * A mutable variant of the Metadata to rapid develop extensions.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MutableMetadata extends CaoMetadata {

	/**
	 * <p>Constructor for MutableMetadata.</p>
	 */
	public MutableMetadata() {
		super(null); //TODO check !
	}

	/**
	 * <p>Constructor for MutableMetadata.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public MutableMetadata(CaoDriver driver) {
		super(driver);
	}

	/**
	 * This method cleanup the internal index. Manipulate the map before you call a getter,
	 * this will recreate the internal index. Changes after it will not affect.
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<CaoMetaDefinition> getMap() {
		synchronized (this) {
			index = null;
			return definition;
		}
	}

}
