package de.mhus.lib.core.jms.test;

import javax.jms.JMSException;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.jms.JmsConnection;
import junit.framework.TestCase;

public class ConnectionTest extends TestCase {

	public void testLifecycle() throws JMSException {
//		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");

		MSingleton.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);

		JmsConnection con = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
		
		con.open();
		
		assertTrue(con.isConnected());
		
		con.close();
		try {
			con.open();
			assertTrue(false);
		} catch (JMSException e) {}
		assertFalse(con.isConnected());
		
		con.reopen();
		
		con.open();
		
		assertTrue(con.isConnected());
		
		con.close();

		assertFalse(con.isConnected());
	}
	
	
	
	
}
