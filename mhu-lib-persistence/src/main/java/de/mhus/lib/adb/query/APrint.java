package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public abstract class APrint {

	public abstract void print(AQuery<?> query, StringBuffer buffer);
	public abstract void getAttributes(AttributeMap map);

}
