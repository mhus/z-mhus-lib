package de.mhus.lib.core.jms.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.util.ObjectContainer;
import de.mhus.lib.jms.ClientJms;
import de.mhus.lib.jms.ClientService;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.jms.ServerService;
import de.mhus.lib.jms.WebServiceDescriptor;
import junit.framework.TestCase;

public class JmsServiceTest extends TestCase {

	public void testCommunication() throws JMSException {
		
		MSingleton.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
		
		JmsConnection con1 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
		JmsConnection con2 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");

		WebServiceDescriptor desc1 = new WebServiceDescriptor(TestJmsService.class);
		ClientService<TestJmsService> client = new ClientService<>(con1.createQueue("test"), desc1);

		TestJmsServiceImp impl = new TestJmsServiceImp();
		WebServiceDescriptor desc2 = new WebServiceDescriptor(impl);
		ServerService<TestJmsService> server = new ServerService<>(con2.createQueue("test"), desc2);
		
		client.open();
		server.open();

		TestJmsService ifc = client.getObject();

		// test one way
		
		impl.lastAction = null;
		ifc.oneWayWithoutReturn();
		while(impl.lastAction == null)
			MThread.sleep(100);
		
		assertEquals("oneWayWithoutReturn", impl.lastAction );

		// test wait
		impl.lastAction = null;
		ifc.withoutReturnValue();
		assertEquals("withoutReturnValue", impl.lastAction );
		
		// test parameters
		impl.lastAction = null;
		ifc.withParameters("a", 1, 2);
		assertEquals("withParameters a12", impl.lastAction );
		
		// test parameters and return
		{
			impl.lastAction = null;
			String ret = ifc.withParametersAndReturn("a", 1, 2);
			assertEquals("withParametersAndReturn a12", impl.lastAction );
			assertEquals("R a", ret);
		}
		
		// test map
		{
			impl.lastAction = null;
			HashMap<String, String> in = new HashMap<>();
			in.put("a", "b");
			Map<String, String> ret = ifc.mapSample(in);
			assertEquals("mapSample {a=b}", impl.lastAction);
			assertEquals("{x=y}", ret.toString());
		}
		
		// test list
		{
			impl.lastAction = null;
			LinkedList<String> in = new LinkedList<>();
			in.add("a");
			List<String> ret = ifc.listSample(in);
			assertEquals("listSample [a]", impl.lastAction);
			assertEquals("[x]", ret.toString());
		}
		
		
		con1.close();
		con2.close();
		
	}
	
	public void testBroadcast() throws JMSException {
			
			MSingleton.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
			
			JmsConnection con1 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
			JmsConnection con2 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
			JmsConnection con3 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");

			WebServiceDescriptor desc1 = new WebServiceDescriptor(TestJmsService.class);
			ClientService<TestJmsService> client = new ClientService<>(con1.createTopic("test"), desc1);

			TestJmsServiceImp impl2 = new TestJmsServiceImp();
			WebServiceDescriptor desc2 = new WebServiceDescriptor(impl2);
			ServerService<TestJmsService> server2 = new ServerService<>(con2.createTopic("test"), desc2);

			TestJmsServiceImp impl3 = new TestJmsServiceImp();
			WebServiceDescriptor desc3 = new WebServiceDescriptor(impl3);
			ServerService<TestJmsService> server3 = new ServerService<>(con3.createTopic("test"), desc3);
			
			client.open();
			server2.open();
			server3.open();

			TestJmsService ifc = client.getObject();

			LinkedList<String> in = new LinkedList<>();
			in.add("a");
			List<String> ret = ifc.listSample(in);
			
			assertEquals("listSample [a]", impl2.lastAction);
			assertEquals("listSample [a]", impl3.lastAction);
//			System.out.println(ret);
			assertEquals("[x, x]", ret.toString());
		
	}
	
}
