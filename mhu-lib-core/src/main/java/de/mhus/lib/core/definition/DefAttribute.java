package de.mhus.lib.core.definition;

import de.mhus.lib.core.MCast;
import de.mhus.lib.errors.MException;

/**
 * <p>DefAttribute class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefAttribute implements IDefAttribute {

	private String name;
	private Object value;

	/**
	 * <p>Constructor for DefAttribute.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public DefAttribute(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent parent) throws MException {
		parent.setString(name, MCast.objectToString(value) );
	}

}
