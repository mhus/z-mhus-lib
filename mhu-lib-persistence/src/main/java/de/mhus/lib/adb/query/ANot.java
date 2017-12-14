package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ANot extends APart {

	private APart operation;

	public ANot(APart operation) {
		this.operation = operation;
	}

	@Override
	public void getAttributes(AttributeMap map) {
		operation.getAttributes(map);
	}

	public APart getOperation() {
		return operation;
	}

}
