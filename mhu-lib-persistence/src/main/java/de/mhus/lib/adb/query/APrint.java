package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public abstract class APrint {

	public void create(AQuery<?> query, AQueryCreator creator) {
		creator.createQuery(this,query);
	}
	public abstract void getAttributes(AttributeMap map);

}
