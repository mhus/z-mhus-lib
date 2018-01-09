package de.mhus.lib.cao;

import java.util.Date;
import java.util.List;

import de.mhus.lib.cao.CaoMetaDefinition.TYPE;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.core.strategy.OperationResult;

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

	public static void deleteRecursive(CaoNode res, int levels) throws CaoException {
		if (levels < 0) return;
		levels--;
		for (CaoNode child : res.getNodes())
			deleteRecursive(child, levels);
		
		CaoAction action = res.getConnection().getAction(CaoAction.DELETE);
		CaoConfiguration config = action.createConfiguration(res, null);
		@SuppressWarnings("unused")
		OperationResult result = action.doExecute(config, null);
		
	}
	
}
