package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>AOrder class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AOrder extends AOperation {

	private String attribute;
	private Class<?> clazz;
	private boolean asc;

	/**
	 * <p>Constructor for AOrder.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param attribute a {@link java.lang.String} object.
	 * @param asc a boolean.
	 */
	public AOrder(Class<?> clazz, String attribute, boolean asc) {
		this.clazz = clazz;
		this.attribute = attribute.toLowerCase();
		this.asc = asc;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append("$db.").append(query.getManager().getMappingName(clazz)).append('.').append(attribute).append('$');
		buffer.append(' ').append(asc ? "ASC" : "DESC");
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
	}

}
