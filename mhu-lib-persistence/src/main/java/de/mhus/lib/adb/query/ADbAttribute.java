package de.mhus.lib.adb.query;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.parser.AttributeMap;

public class ADbAttribute extends AAttribute {

	private String attribute;
	private Class<?> clazz;
	private static Log log = Log.getLog(ADbAttribute.class);

	public ADbAttribute(Class<?> clazz, String attribute) {
		this.clazz = clazz;
		this.attribute = attribute.toLowerCase();
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		Class<?> c = clazz;
		if (c == null) c = query.getType();
		String name = "db." + query.getManager().getMappingName(c) + "." + attribute;
		if (query.getManager().getNameMapping().get(name) == null)
			log.w("mapping dos not exist",name);
		buffer.append("$").append(name).append('$');
	}

	@Override
	public void getAttributes(AttributeMap map) {
	}

}
