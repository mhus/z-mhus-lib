package de.mhus.lib.cao;

import java.util.Date;
import java.util.List;

import de.mhus.lib.cao.CaoMetaDefinition.TYPE;

public class CaoUtil {

	public static TYPE objectToMetaType(Object obj) {
		if (obj == null) return TYPE.OBJECT;
		if (obj instanceof String) return TYPE.STRING;
		if (obj instanceof Boolean) return TYPE.BOOLEAN;
		if (obj instanceof Date) return TYPE.DATETIME;
		if (obj instanceof Double) return TYPE.DOUBLE;
		if (obj instanceof Long) return TYPE.LONG;
		if (obj instanceof List<?>) return TYPE.LIST;
		if (obj instanceof byte[]) return TYPE.BINARY;
		//return TYPE.ELEMENT;
		//return TYPE.TEXT;
		return TYPE.OBJECT;
	}
	
}
