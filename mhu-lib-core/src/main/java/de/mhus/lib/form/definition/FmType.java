package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;

/**
 * <p>FmType class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmType extends DefAttribute {

	/**
	 * <p>Constructor for FmType.</p>
	 *
	 * @param type a {@link de.mhus.lib.form.definition.IFmType} object.
	 */
	public FmType(IFmType type) {
		super("type", type.getValue() );
	}

}
