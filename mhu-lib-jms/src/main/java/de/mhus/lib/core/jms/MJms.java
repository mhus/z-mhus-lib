package de.mhus.lib.core.jms;

import java.util.Enumeration;
import java.util.Map.Entry;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;

public class MJms {

	public static void setProperties(IProperties prop, BytesMessage msg) throws JMSException {
		if (prop == null || msg == null) return;
		for (Entry<String, Object> item : prop) {
			setProperty(item.getKey(),item.getValue(),msg);
		}
	}

	public static void setProperty(String name, Object value, BytesMessage msg) throws JMSException {
		if (name == null || value == null || msg == null) return;

		if (value instanceof String) {
			msg.setStringProperty(name, (String)value);
		} else
		if (value instanceof Boolean) {
			msg.setBooleanProperty(name, (Boolean)value);
		} else
		if (value instanceof Integer) {
			msg.setIntProperty(name, (Integer)value);
		} else
			msg.setObjectProperty(name, value);
		//TODO ...
	}

	public static IProperties getProperties(Message msg) throws JMSException {
		MProperties out = new MProperties();
		if (msg == null) return out;
		@SuppressWarnings("unchecked")
		Enumeration<String> enu = msg.getPropertyNames();
		while (enu.hasMoreElements()) {
			String name = enu.nextElement();
			out.setProperty( name, msg.getObjectProperty(name) );
		}
		return out;
	}

}
