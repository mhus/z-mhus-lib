/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.jms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.config.EmptyConfig;
import de.mhus.lib.core.config.IConfig;

public class MJms {

    private static IConfig config;

    public static void setProperties(IProperties prop, Message msg) throws JMSException {
        setProperties("", prop, msg);
    }

    public static void setProperties(String prefix, IProperties prop, Message msg)
            throws JMSException {
        if (prop == null || msg == null) return;
        for (Entry<String, Object> item : prop) {
            setProperty(prefix + item.getKey(), item.getValue(), msg);
        }
    }

    public static void setProperty(String name, Object value, Message msg) throws JMSException {
        if (value == null || msg == null || name == null) return;
        if (value instanceof String) msg.setStringProperty(name, (String) value);
        else if (value instanceof Boolean) msg.setBooleanProperty(name, (Boolean) value);
        else if (value instanceof Integer) msg.setIntProperty(name, (Integer) value);
        else if (value instanceof Long) msg.setLongProperty(name, (Long) value);
        else if (value instanceof Double) msg.setDoubleProperty(name, (Double) value);
        else if (value instanceof Byte) msg.setByteProperty(name, (Byte) value);
        else if (value instanceof Float) msg.setFloatProperty(name, (Float) value);
        else if (value instanceof Short) msg.setShortProperty(name, (Short) value);
        else if (value instanceof Date) msg.setStringProperty(name, MDate.toIso8601((Date) value));
        else msg.setStringProperty(name, String.valueOf(value));
    }

    public static IProperties getProperties(Message msg) throws JMSException {
        MProperties out = new MProperties();
        if (msg == null) return out;
        @SuppressWarnings("unchecked")
        Enumeration<String> enu = msg.getPropertyNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            out.setProperty(name, msg.getObjectProperty(name));
        }
        return out;
    }

    public static void setMapProperties(Map<?, ?> prop, MapMessage msg) throws JMSException {
        setMapProperties("", prop, msg);
    }

    public static void setMapProperties(String prefix, Map<?, ?> prop, MapMessage msg)
            throws JMSException {
        if (prop == null || msg == null) return;
        for (Entry<?, ?> item : prop.entrySet()) {
            setMapProperty(prefix + item.getKey(), item.getValue(), msg);
        }
    }

    public static void setMapProperty(String name, Object value, MapMessage msg)
            throws JMSException {
        if (msg == null || name == null) return;

        if (value == null) msg.setObject(name, null);
        else if (value instanceof String) msg.setString(name, (String) value);
        else if (value instanceof Boolean) msg.setBoolean(name, (Boolean) value);
        else if (value instanceof Integer) msg.setInt(name, (Integer) value);
        else if (value instanceof Long) msg.setLong(name, (Long) value);
        else if (value instanceof Double) msg.setDouble(name, (Double) value);
        else if (value instanceof Byte) msg.setByte(name, (Byte) value);
        else if (value instanceof Float) msg.setFloat(name, (Float) value);
        else if (value instanceof Short) msg.setShort(name, (Short) value);
        else if (value instanceof Date) msg.setString(name, MDate.toIso8601((Date) value));
        else msg.setString(name, String.valueOf(value));
    }

    public static IProperties getMapProperties(MapMessage msg) throws JMSException {
        MProperties out = new MProperties();
        if (msg == null) return out;
        @SuppressWarnings("unchecked")
        Enumeration<String> enu = msg.getMapNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            out.setProperty(name, msg.getObject(name));
        }
        return out;
    }

    public static Object toPrimitive(Object in) {
        if (in == null) return null;
        if (in.getClass().isPrimitive()) return in;
        if (in instanceof Date) return ((Date) in).getTime();
        return String.valueOf(in);
    }

    public static synchronized IConfig getConfig() {
        if (config == null) config = MApi.get().getCfgManager().getCfg("jms", new EmptyConfig());
        return config;
    }

    public static boolean isMapProperty(Object value) {
        return value == null
                || value.getClass().isPrimitive()
                || value instanceof String
                || value instanceof Boolean
                || value instanceof Integer
                || value instanceof Long
                || value instanceof Double
                || value instanceof String
                || value instanceof Byte
                || value instanceof Float
                || value instanceof Short
                || value instanceof Date // use MDate
                || value instanceof UUID // to String
        ;
    }

    public static byte[] read(BytesMessage msg) throws JMSException {
        long len = msg.getBodyLength();
        byte[] bytes = new byte[(int) len];
        msg.readBytes(bytes);
        return bytes;
    }

    public static void read(BytesMessage msg, OutputStream os) throws JMSException, IOException {
        byte[] bytes = new byte[1024];
        while (true) {
            int i = msg.readBytes(bytes);
            if (i < 0) return;
            if (i == 0) MThread.sleep(200);
            else os.write(bytes, 0, i);
        }
    }

    public static void write(InputStream is, BytesMessage msg) throws JMSException, IOException {
        byte[] bytes = new byte[1024];
        while (true) {
            int i = is.read(bytes);
            if (i < 0) return;
            if (i == 0) MThread.sleep(200);
            else msg.writeBytes(bytes, 0, i);
        }
    }
}
