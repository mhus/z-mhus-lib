package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ALiteral extends APart {

	private String literal;

	public ALiteral(String literal) {
		this.literal = literal;
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

	public String getLiteral() {
		return literal;
	}

}
