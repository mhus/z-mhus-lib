package de.mhus.lib.core.parser;

import java.util.HashMap;


public class AttributeMap extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8679000665603785877L;

	public AttributeMap() {
	}
	
	public AttributeMap(Object ... entries) {
		if (entries == null) return;
		for (int i = 0; i < entries.length; i+=2) {
			put(String.valueOf(entries[i]),entries[i+1]);
		}
	}
	
}
