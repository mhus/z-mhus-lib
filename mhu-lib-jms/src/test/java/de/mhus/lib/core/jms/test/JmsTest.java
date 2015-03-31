package de.mhus.lib.core.jms.test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.util.ObjectContainer;
import de.mhus.lib.jms.ClientJms;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerJms;
import junit.framework.TestCase;

public class JmsTest extends TestCase {

	public void testCommunication() throws JMSException {
		
		MSingleton.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);

		JmsConnection con1 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
		JmsConnection con2 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");

		ClientJms client = new ClientJms(con1.createQueue("test"));

		final ObjectContainer<Message> requestMessage = new ObjectContainer<>();
		
		ServerJms server = new ServerJms(con2.createQueue("test")) {

			@Override
			public void receivedOneWay(Message msg) throws JMSException {
				System.out.println("receivedOneWay: " + msg);
				requestMessage.setObject(msg);
			}

			@Override
			public Message received(Message msg) throws JMSException {
				System.out.println("received: " + msg);
				
				requestMessage.setObject(msg);
				return getSession().createTextMessage("pong");
			}
			
		};
		
		client.open();
		server.open();
		
		client.sendJmsOneWay(con1.getSession().createTextMessage("aloa"));

		while(requestMessage.getObject() == null)
			MThread.sleep(100);
		assertEquals("aloa", ((TextMessage)requestMessage.getObject()).getText() );
		
		Message res = client.sendJms(con1.getSession().createTextMessage("ping"));
		assertEquals("ping", ((TextMessage)requestMessage.getObject()).getText() );
		assertEquals("pong", ((TextMessage)res).getText() );

		System.out.println(res);
		
		con1.close();
		con2.close();
		
	}
	
}
