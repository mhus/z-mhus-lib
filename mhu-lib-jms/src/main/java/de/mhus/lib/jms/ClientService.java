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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.NotSupportedException;

public class ClientService<T> extends ClientJms {

    public static final String PROP_FUNCTION_NAME = "de.mhus.lib.jms.function";

    public static final String PROP_EXCEPION_TYPE = "de.mhus.lib.jms.exception.type";

    public static final String PROP_DIRECT_MSG = "de.mhus.lib.jms.direct";

    public static final String PROP_EXCEPION_TEXT = "de.mhus.lib.jmsexception.text";

    public static final String PROP_EXCEPION_CLASS = "de.mhus.lib.jmsexception.class";

    public static final String PROP_EXCEPTION_METHOD = "de.mhus.lib.jmsexception.method";

    private ServiceDescriptor desc;
    private T proxy;
    private ClassLoader classLoader = getClass().getClassLoader();

    public ClientService(JmsDestination dest, ServiceDescriptor desc) {
        super(dest);
        this.desc = desc;
        createProxy();
    }

    @SuppressWarnings("unchecked")
    protected void createProxy() {
        Class<?> ifc = desc.getInterface();
        if (ifc == null) return;

        InvocationHandler handler = new MyInvocationHandler();

        proxy = (T) Proxy.newProxyInstance(ifc.getClassLoader(), new Class[] {ifc}, handler);
    }

    public T getClientProxy() {
        return proxy;
    }

    private class MyInvocationHandler implements InvocationHandler {

        @SuppressWarnings("rawtypes")
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            String name = method.getName().toLowerCase();

            FunctionDescriptor fDesc = desc.getFunction(name);
            if (fDesc == null) {
                //				log().w("function not found", name);
                throw new MRuntimeException("function not found", name);
            }

            Message msg = null;
            if (args != null && args.length == 1 && args[0] instanceof Message) {
                msg = (Message) args[0];
                msg.setBooleanProperty(PROP_DIRECT_MSG, true);
            } else msg = getSession().createObjectMessage(args);

            msg.setStringProperty(PROP_FUNCTION_NAME, method.getName().toLowerCase());

            if (fDesc.isOneWay() || dest.isTopic() && fDesc.getReturnType() == Void.class) {

                try {
                    sendJmsOneWay(msg);
                } catch (Exception e) {
                    log().w(
                                    "internal error",
                                    desc.getInterface().getCanonicalName(),
                                    method.getName(),
                                    e);
                }
            } else if (dest.isTopic() && fDesc.getReturnType() == List.class) {
                try {
                    Message[] answers = sendJmsBroadcast(msg);

                    LinkedList<Object> out = new LinkedList<>();

                    for (Message answer : answers) {
                        if (answer.getStringProperty(PROP_EXCEPION_TYPE) == null) {
                            if (answer.propertyExists(PROP_DIRECT_MSG)
                                    && answer.getBooleanProperty(PROP_DIRECT_MSG)) {
                                out.add(answer);
                            } else if (answer instanceof ObjectMessage) {
                                Serializable answerObj = ((ObjectMessage) answer).getObject();
                                if (answerObj instanceof Collection) {
                                    for (Object colItem : (Collection) answerObj) out.add(colItem);
                                } else out.add(answerObj);
                            }
                        }
                    }

                    return out;
                } catch (Exception e) {
                    log().w(
                                    "internal error",
                                    desc.getInterface().getCanonicalName(),
                                    method.getName(),
                                    e);
                }

            } else {
                Message res = null;
                try {
                    res = sendJms(msg);
                    // check success and throw exceptions
                    if (res == null)
                        throw new MRuntimeException(
                                "internal error: result is null",
                                desc.getInterface().getCanonicalName(),
                                method.getName());
                } catch (Exception e) {
                    log().w(
                                    "internal error",
                                    desc.getInterface().getCanonicalName(),
                                    method.getName(),
                                    e);
                }

                String exceptionType = res.getStringProperty(PROP_EXCEPION_TYPE);
                if (exceptionType != null) {
                    Class<?> exceptionClass = getClassLoader().loadClass(exceptionType);
                    Throwable exception = null;
                    try {
                        Constructor<?> constructor = exceptionClass.getConstructor(String.class);
                        exception =
                                (Throwable)
                                        constructor.newInstance(
                                                res.getStringProperty(PROP_EXCEPION_TEXT)
                                                        + " ["
                                                        + res.getStringProperty(PROP_EXCEPION_CLASS)
                                                        + "."
                                                        + res.getStringProperty(
                                                                PROP_EXCEPTION_METHOD)
                                                        + "]");
                    } catch (Throwable t) {
                        exception =
                                (Throwable) exceptionClass.getDeclaredConstructor().newInstance();
                    }
                    throw exception;
                }

                try {
                    try {
                        if (Message.class.isAssignableFrom(method.getReturnType())
                                && res.getBooleanProperty(PROP_DIRECT_MSG)) return res;
                    } catch (JMSException e) {
                    }

                    if (res instanceof ObjectMessage) {
                        ObjectMessage om = (ObjectMessage) res;
                        return om.getObject();
                    }

                    if (res instanceof BytesMessage) {
                        BytesMessage bm = (BytesMessage) res;
                        long len = Math.min(bm.getBodyLength(), 1024 * 1024 * 1024 * 100); // 100 MB
                        byte[] buffer = new byte[(int) len];
                        bm.readBytes(buffer);
                        return buffer;
                    }

                    if (res instanceof TextMessage) {
                        TextMessage tm = (TextMessage) res;
                        return tm.getText();
                    }

                    //					if (res instanceof MapMessage) {
                    //						MapMessage mm = (MapMessage)res;
                    //					}

                    throw new NotSupportedException(
                            "message type is not supported", res.getClass().getCanonicalName());
                } catch (Exception e) {
                    log().w(
                                    "internal error",
                                    desc.getInterface().getCanonicalName(),
                                    method.getName(),
                                    e);
                }
            }

            return null;
        }
    }

    public Class<?> getInterface() {
        return desc.getInterface();
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public T getObject() {
        return (T) getClientProxy();
    }

    public void sendJsonOneWay(IProperties prop, JsonNode json) throws JMSException, IOException {

        ByteArrayOutputStream w = new ByteArrayOutputStream();
        try {
            MJson.save(json, w);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }

        open();
        BytesMessage msg = getJmsDestination().getConnection().createBytesMessage();
        MJms.setProperties(prop, msg);
        msg.writeBytes(w.toByteArray());
        sendJmsOneWay(msg);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
