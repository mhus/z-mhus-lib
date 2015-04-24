package de.mhus.lib.adb.model;

import de.mhus.lib.errors.MException;


public class FeatureCut extends Feature {

	private boolean cutAll;

	@Override
	public void doInit() throws MException {
		cutAll = table.getAttributes().getBoolean("cut_all", false);
	}

	@Override
	public Object getValue(Object obj, Field field, Object val) throws MException {

		if ( ( cutAll || field.getAttributes().getBoolean("cut",false) ) && val != null && val instanceof String && ((String)val).length() > field.getSize()) {
			log().t("cut",field);
			val = ((String)val).substring(0,field.getSize());
		}
		return val;
	}

}
