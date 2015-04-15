package de.mhus.examples.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerJms;

public class DemoReceiver {

	public static void main(String[] args) throws JMSException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		@SuppressWarnings("unused")
		ServerJms server = new ServerJms(con.createQueue("mike")) {

			@Override
			public void receivedOneWay(Message msg) throws JMSException {
				System.out.println("Received: " + msg);
			}

			@Override
			public Message received(Message msg) throws JMSException {
				System.out.println("Received: " + msg);
				return null;
			}
			
		};
		
		
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
