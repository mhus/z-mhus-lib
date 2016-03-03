package de.mhus.lib.adb.model;

import de.mhus.lib.adb.DbManager;

/**
 * <p>AttributeFeatureCut class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AttributeFeatureCut implements AttributeFeature {

	/** Constant <code>NAME="cut"</code> */
	public final static String NAME = "cut";
	
	//	private Field field;
	private int size;

	/** {@inheritDoc} */
	@Override
	public void init(DbManager manager, Field field) {
		//		this.field = field;
		size = field.getSize();
	}

	/** {@inheritDoc} */
	@Override
	public Object set(Object pojo, Object value) {
		if (value != null && value instanceof String && ((String)value).length() > size)
			value = ((String)value).substring(0, size);
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public Object get(Object pojo, Object value) {
		if (value != null && value instanceof String && ((String)value).length() > size)
			value = ((String)value).substring(0, size);
		return value;
	}

}
