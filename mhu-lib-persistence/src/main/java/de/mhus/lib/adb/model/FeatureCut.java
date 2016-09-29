package de.mhus.lib.adb.model;

import de.mhus.lib.errors.MException;


/**
 * <p>FeatureCut class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FeatureCut extends Feature {

	/** Constant <code>NAME</code> */
	public static final Object NAME = "cut";
	private boolean cutAll;

	/** {@inheritDoc} */
	@Override
	public void doInit() throws MException {
		cutAll = table.getAttributes().getBoolean("cut_all", false);
	}

	/** {@inheritDoc} */
	@Override
	public Object getValue(Object obj, Field field, Object val) throws MException {

		if ( ( cutAll || field.getAttributes().getBoolean("cut",false) ) && val != null && val instanceof String && ((String)val).length() > field.getSize()) {
			log().t("cut",field);
			val = ((String)val).substring(0,field.getSize());
		}
		return val;
	}

}
