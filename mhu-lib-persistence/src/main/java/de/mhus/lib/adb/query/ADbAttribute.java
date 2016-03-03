package de.mhus.lib.adb.query;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ADbAttribute class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ADbAttribute extends AAttribute {

	private String attribute;
	private Class<?> clazz;
	private static Log log = Log.getLog(ADbAttribute.class);

	/**
	 * <p>Constructor for ADbAttribute.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param attribute a {@link java.lang.String} object.
	 */
	public ADbAttribute(Class<?> clazz, String attribute) {
		this.clazz = clazz;
		this.attribute = attribute.toLowerCase();
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		Class<?> c = clazz;
		if (c == null) c = query.getType();
		String name = "db." + query.getManager().getMappingName(c) + "." + attribute;
		if (query.getManager().getNameMapping().get(name) == null)
			log.w("mapping dos not exist",name);
		buffer.append("$").append(name).append('$');
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
	}

}
