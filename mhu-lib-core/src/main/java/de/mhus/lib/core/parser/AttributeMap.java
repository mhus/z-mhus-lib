package de.mhus.lib.core.parser;

import java.util.HashMap;


/**
 * <p>AttributeMap class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AttributeMap extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8679000665603785877L;

	/**
	 * <p>Constructor for AttributeMap.</p>
	 */
	public AttributeMap() {
	}
	
	/**
	 * <p>Constructor for AttributeMap.</p>
	 *
	 * @param entries a {@link java.lang.Object} object.
	 */
	public AttributeMap(Object ... entries) {
		if (entries == null) return;
		for (int i = 0; i < entries.length; i+=2) {
			put(String.valueOf(entries[i]),entries[i+1]);
		}
	}
	
}
