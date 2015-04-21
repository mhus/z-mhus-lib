package de.mhus.lib.jms;

import javax.jms.Message;

public interface JmsInterceptor {

	void begin(Message message);

	void end(Message message);

	void prepare(Message answer);

	void answer(Message message);

}
