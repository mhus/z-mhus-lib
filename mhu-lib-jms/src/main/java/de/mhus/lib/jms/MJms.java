package de.mhus.lib.jms;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;

public class MJms {

	public static void setProperties(IProperties prop, Message msg) throws JMSException {
		setProperties("",prop, msg);
	}
	
	public static void setProperties(String prefix, IProperties prop, Message msg) throws JMSException {
		if (prop == null || msg == null) return;
		for (Entry<String, Object> item : prop) {
			setProperty(prefix + item.getKey(),item.getValue(),msg);
		}
	}

	public static void setProperty(String name, Object value, Message msg) throws JMSException {
		if (value == null || msg == null || name == null) return;
		if (value instanceof String)
			msg.setStringProperty(name, (String)value);
		else
		if (value instanceof Boolean)
			msg.setBooleanProperty(name, (Boolean)value);
		else
		if (value instanceof Integer)
			msg.setIntProperty(name, (Integer)value);
		else
		if (value instanceof Long)
			msg.setLongProperty(name, (Long)value);
		else
		if (value instanceof Double)
			msg.setDoubleProperty(name, (Double)value);
		else
		if (value instanceof Byte)
			msg.setByteProperty(name, (Byte)value);
		else
		if (value instanceof Float)
			msg.setFloatProperty(name, (Float)value);
		else
		if (value instanceof Short)
			msg.setShortProperty(name, (Short)value);
		else
			msg.setObjectProperty(name, value);
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
	
	public static void setMapProperties(IProperties prop, MapMessage msg) throws JMSException {
		setMapProperties("", prop, msg);
	}
	
	public static void setMapProperties(String prefix, IProperties prop, MapMessage msg) throws JMSException {
		if (prop == null || msg == null) return;
		for (Entry<String, Object> item : prop) {
			setMapProperty(prefix + item.getKey(),item.getValue(),msg);
		}
	}

	public static void setMapProperty(String name, Object value, MapMessage msg) throws JMSException {
		if (value == null || msg == null || name == null) return;
		if (value instanceof String)
			msg.setString(name, (String)value);
		else
		if (value instanceof Boolean)
			msg.setBoolean(name, (Boolean)value);
		else
		if (value instanceof Integer)
			msg.setInt(name, (Integer)value);
		else
		if (value instanceof Long)
			msg.setLong(name, (Long)value);
		else
		if (value instanceof Double)
			msg.setDouble(name, (Double)value);
		else
		if (value instanceof Byte)
			msg.setByte(name, (Byte)value);
		else
		if (value instanceof Float)
			msg.setFloat(name, (Float)value);
		else
		if (value instanceof Short)
			msg.setShort(name, (Short)value);
		else
			msg.setObject(name, value);
	}

	public static IProperties getMapProperties(MapMessage msg) throws JMSException {
		MProperties out = new MProperties();
		if (msg == null) return out;
		@SuppressWarnings("unchecked")
		Enumeration<String> enu = msg.getMapNames();
		while (enu.hasMoreElements()) {
			String name = enu.nextElement();
			out.setProperty( name, msg.getObject(name) );
		}
		return out;
	}
	
	public static Object toPrimitive(Object in) {
		if (in == null) return null;
		if (in.getClass().isPrimitive()) return in;
		if (in instanceof Date) return ((Date)in).getTime();
		return String.valueOf(in);
	}
	
}
