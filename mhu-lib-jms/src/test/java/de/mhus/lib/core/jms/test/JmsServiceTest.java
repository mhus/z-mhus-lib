/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.jms.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.lang.ValueProvider;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.jms.ClientObjectProxy;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerObjectProxy;
import de.mhus.lib.jms.WebServiceDescriptor;

public class JmsServiceTest {

	@Test() // timeout=120000
	public void testCommunication() throws JMSException {
		
		MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
		
		JmsConnection con1 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
		JmsConnection con2 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");

		WebServiceDescriptor desc1 = new WebServiceDescriptor(TestJmsService.class);
		ClientObjectProxy<TestJmsService> client = new ClientObjectProxy<>(con1.createQueue("test"), desc1);

		TestJmsServiceImp impl = new TestJmsServiceImp();
		WebServiceDescriptor desc2 = new WebServiceDescriptor(impl);
		ServerObjectProxy<TestJmsService> server = new ServerObjectProxy<>(con2.createQueue("test"), desc2);
		
		client.open();
		server.open();

		TestJmsService ifc = client.getObject();

		// test one way
		System.out.println(">>> Test One Way");
		impl.lastAction = null;
		ifc.oneWayWithoutReturn();
		while(impl.lastAction == null)
			MThread.sleep(100);
		
		assertEquals("oneWayWithoutReturn", impl.lastAction );

		// test wait
		System.out.println(">>> Test Wait");
		impl.lastAction = null;
		ifc.withoutReturnValue();
		assertEquals("withoutReturnValue", impl.lastAction );
		
		// test parameters
		System.out.println(">>> Test Parameters");
		impl.lastAction = null;
		ifc.withParameters("a", 1, 2);
		assertEquals("withParameters a12", impl.lastAction );
		
		// test parameters and return
		{
			System.out.println(">>> Test Parameters and Return");
			impl.lastAction = null;
			String ret = ifc.withParametersAndReturn("a", 1, 2);
			assertEquals("withParametersAndReturn a12", impl.lastAction );
			assertEquals("R a", ret);
		}
		
		// test map
		{
			System.out.println(">>> Test MAP");
			impl.lastAction = null;
			HashMap<String, String> in = new HashMap<>();
			in.put("a", "b");
			Map<String, String> ret = ifc.mapSample(in);
			assertEquals("mapSample {a=b}", impl.lastAction);
			assertEquals("{x=y}", ret.toString());
		}
		
		// test list
		{
			System.out.println(">>> Test List");
			impl.lastAction = null;
			LinkedList<String> in = new LinkedList<>();
			in.add("a");
			List<String> ret = ifc.listSample(in);
			assertEquals("listSample [a]", impl.lastAction);
			assertEquals("[x]", ret.toString());
		}
		
		// test raw message
		{
			System.out.println(">>> Test Raw");
			impl.lastAction = null;
			TextMessage msg = client.createTextMessage("works");
			ifc.receiveMessage(msg);
			assertEquals("receiveMessage works", impl.lastAction);
		}
		
		{
			System.out.println(">>> Test Text");
			impl.lastAction = null;
			Message msg = ifc.sendMessage("text");
			assertEquals("sendMessage text", impl.lastAction);
			assertEquals("text", ((TextMessage)msg).getText());
		}
		
		// test exception
		{
			System.out.println(">>> Test Exception");
			impl.lastAction = null;
			try {
				ifc.throwException("ex");
				assertTrue(false);
			} catch (IOException e) {
				e.printStackTrace();
				assertEquals("ex [de.mhus.lib.core.jms.test.TestJmsServiceImp.throwexception]",e.getMessage());
			}
			assertEquals("throwException ex", impl.lastAction);
		}	
		
		System.out.println(">>> Close");
		con1.close();
		con2.close();
		
		client.close();
		server.close();
	}
	
	@Test() //timeout=120000
	public void testBroadcast() throws JMSException {
			
			MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
			
			JmsConnection con1 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
			JmsConnection con2 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
			JmsConnection con3 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");

			WebServiceDescriptor desc1 = new WebServiceDescriptor(TestJmsService.class);
			ClientObjectProxy<TestJmsService> client = new ClientObjectProxy<>(con1.createTopic("test"), desc1);

			TestJmsServiceImp impl2 = new TestJmsServiceImp();
			WebServiceDescriptor desc2 = new WebServiceDescriptor(impl2);
			ServerObjectProxy<TestJmsService> server2 = new ServerObjectProxy<>(con2.createTopic("test"), desc2);

			TestJmsServiceImp impl3 = new TestJmsServiceImp();
			WebServiceDescriptor desc3 = new WebServiceDescriptor(impl3);
			ServerObjectProxy<TestJmsService> server3 = new ServerObjectProxy<>(con3.createTopic("test"), desc3);
			
			client.setBroadcastTimeout(3000); // 5 sec to wait for answers
			client.open();
			server2.open();
			server3.open();

			TestJmsService ifc = client.getObject();
			
			{
				LinkedList<String> in = new LinkedList<>();
				in.add("a");
				System.out.println(">>> Test Broadcast");
				@SuppressWarnings("unused")
				List<String> ret = ifc.listSample(in);
				assertEquals("listSample [a]", impl2.lastAction);
				assertEquals("listSample [a]", impl3.lastAction);
				//TODO assertEquals("[x, x]", ret.toString());
			}
		
			{
				// test one way
				impl2.lastAction = null;
				impl3.lastAction = null;
				ifc.oneWayWithoutReturn();
				MThread.getWithTimeout(new ValueProvider<String>() {
						@Override
						public String getValue() throws Exception {
							if (impl2.lastAction == null) return null;
							if (impl3.lastAction == null) return null;
							return "ready";
						}
					}, 15000, false);
				assertEquals("oneWayWithoutReturn", impl2.lastAction);
				assertEquals("oneWayWithoutReturn", impl3.lastAction);
			}
			
			
			// This case is not supported yet. The case "broadcast and raw messages" is extremely rare
			
//			{
//				List<Message> ret = ifc.messageBroadcast("b");
//				assertEquals("messageBroadcast b", impl2.lastAction);
//				assertEquals("messageBroadcast b", impl3.lastAction);
//				System.out.println(ret);
//				assertEquals(2, ret.size());
//			}
			
			con1.close();
			con2.close();
			con3.close();
			
			client.close();
			server2.close();
			server3.close();
			
	}
	
}
