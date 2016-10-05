package de.mhus.lib.logging.parameter;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.ParameterMapper;

public abstract class AbstractParameterMapper implements ParameterMapper {

	@Override
	public Object[] map(Log log, Object[] msg) {
		if (msg == null) return msg;
		for (int i = 0; i < msg.length; i++) {
			Object o = msg[i];
			if (o == null) continue;
			o = map(o);
			if (o != null) msg[i] = o;
		}
		return msg;
	}

	protected abstract Object map(Object o);
	
}
