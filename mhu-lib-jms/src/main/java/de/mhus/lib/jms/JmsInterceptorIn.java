package de.mhus.lib.jms;

import javax.jms.Message;

public interface JmsInterceptorIn {

	void begin(Message message);

	void end(Message message);

}
