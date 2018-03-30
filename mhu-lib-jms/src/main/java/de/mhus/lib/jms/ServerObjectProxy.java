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
package de.mhus.lib.jms;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import de.mhus.lib.core.IProperties;

public class ServerObjectProxy<T> extends ServerJms implements JmsObjectProxy {

	private ServiceDescriptor service;

	public ServerObjectProxy(JmsDestination dest, ServiceDescriptor service) {
		super(dest);
		this.service = service;
	}

	@Override
	public void receivedOneWay(Message msg) throws JMSException {
		
		String functionName = msg.getStringProperty(ClientObjectProxy.PROP_FUNCTION_NAME);
		if (functionName == null) {
			log().w("function not set",getJmsDestination());
			return;
		}
		functionName = functionName.toLowerCase();
		FunctionDescriptor function = service.getFunction(functionName);
		if (function == null) {
			log().w("function not found",functionName,getJmsDestination());
			return;
		}
		if (!function.isOneWay()) {
			log().w("function not one way",functionName,getJmsDestination());
			return;
		}
		IProperties properties = MJms.getProperties(msg);
		
		Object[] obj = null;
		if (msg.propertyExists(ClientObjectProxy.PROP_DIRECT_MSG) && msg.getBooleanProperty(ClientObjectProxy.PROP_DIRECT_MSG)) {
			obj = new Object[] { msg };
		} else
		if (msg instanceof ObjectMessage) {
			obj = (Object[]) ((ObjectMessage)msg).getObject();
		} else
		if (msg instanceof TextMessage) {
			obj = new Object[] { ((TextMessage)msg).getText() };
		} else
		if (msg instanceof BytesMessage) {
			BytesMessage bm = (BytesMessage)msg;
			long len = Math.min(bm.getBodyLength(), 1024 * 1024 * 1024 * 100); // 100 MB
			byte[] buffer = new byte[(int) len];
			bm.readBytes(buffer);
			obj = new Object[] { buffer };
		}
		
		function.doExecute(properties, obj);
		
	}

	@Override
	public Message received(Message msg) throws JMSException {
		
		String functionName = msg.getStringProperty(ClientObjectProxy.PROP_FUNCTION_NAME);
		if (functionName == null) {
			log().w("function not set",getJmsDestination());
			return null;
		}
		functionName = functionName.toLowerCase();
		FunctionDescriptor function = service.getFunction(functionName);
		if (function == null) {
			log().w("function not found",functionName,getJmsDestination());
			return null;
		}
		
		IProperties properties = MJms.getProperties(msg);
		
		Object[] obj = null;
		if (msg.propertyExists(ClientObjectProxy.PROP_DIRECT_MSG) && msg.getBooleanProperty(ClientObjectProxy.PROP_DIRECT_MSG)) {
			obj = new Object[] { msg };
		} else
		if (msg instanceof ObjectMessage) {
			obj = (Object[]) ((ObjectMessage)msg).getObject();
		} else
		if (msg instanceof TextMessage) {
			obj = new Object[] { ((TextMessage)msg).getText() };
		} else
		if (msg instanceof BytesMessage) {
			BytesMessage bm = (BytesMessage)msg;
			long len = Math.min(bm.getBodyLength(), 1024 * 1024 * 1024 * 100); // 100 MB
			byte[] buffer = new byte[(int) len];
			bm.readBytes(buffer);
			obj = new Object[] { buffer };
		}

		try {
			RequestResult<Object> res = function.doExecute(properties, obj);
			
			Message out = null;
			if (res == null || res.getResult() == null) {
				out = getSession().createObjectMessage(null);
			} else
			if (res.getResult() instanceof Message) {
				out = (Message) res.getResult();
				out.setBooleanProperty(ClientObjectProxy.PROP_DIRECT_MSG, true);
			} else {
				out = getSession().createObjectMessage((Serializable) res.getResult());
			}
			if (res != null)
				MJms.setProperties(res.getProperties(), out);
			
			return out;
			
		} catch (Throwable t) {
			
			
			
			
		}
		

		return null;
	}

	@Override
	protected void onOpen() {
		T o = getObject();
		if (o != null && o instanceof JmsServiceListener) {
			((JmsServiceListener)o).jmsServiceOnOpen(this);
		}
	}

	@Override
	protected void onReset() {
		T o = getObject();
		if (o != null && o instanceof JmsServiceListener) {
			((JmsServiceListener)o).jmsServiceOnReset(this);
		}
	}
	
	@Override
	public Class<?> getInterface() {
		return service.getInterface();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject() {
		return (T)service.getObject();
	}

}
