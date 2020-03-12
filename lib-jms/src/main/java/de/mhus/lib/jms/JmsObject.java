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

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import de.mhus.lib.basics.MCloseable;
import de.mhus.lib.core.MLog;

public abstract class JmsObject extends MLog implements MCloseable {

    protected boolean closed = false;

    public abstract void open() throws JMSException;

    public abstract void reset();

    protected void setClosed() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        log().d("close");
        reset();
        setClosed();
    }

    public abstract Session getSession();

    public abstract boolean isConnected();

    public abstract JmsDestination getJmsDestination();

    public void reopen() throws JMSException {
        closed = false;
        reset();
        closed = false;
        open();
    }

    public BytesMessage createBytesMessage() throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createBytesMessage();
    }

    public MapMessage createMapMessage() throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createMapMessage();
    }

    public Message createMessage() throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createObjectMessage();
    }

    public ObjectMessage createObjectMessage(Serializable arg0) throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createObjectMessage(arg0);
    }

    public StreamMessage createStreamMessage() throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createStreamMessage();
    }

    public TextMessage createTextMessage() throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createTextMessage();
    }

    public TextMessage createTextMessage(String arg0) throws JMSException {
        if (getSession() == null) throw new JMSException("not connected " + getJmsDestination());
        return getSession().createTextMessage(arg0);
    }
}
