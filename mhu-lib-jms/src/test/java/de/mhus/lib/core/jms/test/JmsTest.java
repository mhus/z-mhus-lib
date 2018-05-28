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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.util.ObjectContainer;
import de.mhus.lib.jms.ClientJms;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerJms;
import junit.framework.TestCase;

public class JmsTest extends TestCase {

	public void testCommunication() throws JMSException {
		
		MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);

		JmsConnection con1 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");
		JmsConnection con2 = new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");

		ClientJms client = new ClientJms(con1.createQueue("test"));

		final ObjectContainer<Message> requestMessage = new ObjectContainer<>();
		
		ServerJms server = new ServerJms(con2.createQueue("test")) {

			@Override
			public void receivedOneWay(Message msg) throws JMSException {
				System.out.println("--- receivedOneWay: " + msg);
				requestMessage.setObject(msg);
			}

			@Override
			public Message received(Message msg) throws JMSException {
				System.out.println("--- received: " + msg);
				
				requestMessage.setObject(msg);
				return getSession().createTextMessage("pong");
			}
			
		};
		
		client.open();
		server.open();
		
		System.out.println(">>> sendJmsOneWay");
		client.sendJmsOneWay(con1.createTextMessage("aloa"));

		while(requestMessage.getObject() == null)
			MThread.sleep(100);
		assertEquals("aloa", ((TextMessage)requestMessage.getObject()).getText() );
		
		System.out.println(">>> sendJms");
		Message res = client.sendJms(con1.createTextMessage("ping"));
		assertEquals("ping", ((TextMessage)requestMessage.getObject()).getText() );
		assertEquals("pong", ((TextMessage)res).getText() );

		System.out.println(res);
		
		con1.close();
		con2.close();
		
	}
	
}
