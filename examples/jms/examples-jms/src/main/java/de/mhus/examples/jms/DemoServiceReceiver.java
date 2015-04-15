package de.mhus.examples.jms;

import javax.jms.JMSException;

import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerService;
import de.mhus.lib.jms.WebServiceDescriptor;

public class DemoServiceReceiver {


	public static void main(String[] args) throws JMSException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		DummyServiceImpl service = new DummyServiceImpl();
		WebServiceDescriptor descriptor = new WebServiceDescriptor(service);
		@SuppressWarnings("rawtypes")
		ServerService server = new ServerService(con.createQueue("mike"), descriptor);
		
		server.open();
		
		while(true) {
			try {
				con.doChannelBeat();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			MThread.sleep(10000);
		}
	}

}
