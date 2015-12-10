package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;

public class FmType extends DefAttribute {

	public FmType(IFmType type) {
		super("type", type.getValue() );
	}

}
