package de.mhus.lib.jms.util;

import javax.jms.Message;

import de.mhus.lib.core.logging.ParameterEntryMapper;

public class MessageParameterMapper implements ParameterEntryMapper {

	@Override
	public Object map(Object o) {
		if (o instanceof Message)
			return new MessageStringifier((Message)o);
		return null;
	}

}
