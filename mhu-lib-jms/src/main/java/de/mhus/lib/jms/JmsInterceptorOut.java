package de.mhus.lib.jms;

import javax.jms.Message;

public interface JmsInterceptorOut {

	void prepare(Message answer);

}
