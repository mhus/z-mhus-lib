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
	public void getAttributes(AttributeMap map) {
	}

	public String getAttribute() {
		return attribute;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
