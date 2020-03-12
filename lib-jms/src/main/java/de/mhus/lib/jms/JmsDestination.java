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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import de.mhus.lib.core.MSystem;

public class JmsDestination extends JmsObject {

    protected JmsConnection con;
    private String destination;
    private boolean destinationTopic;
    private Destination jmsDestination;

    public JmsDestination(String destination, boolean destinationTopic) {
        this.destination = destination;
        this.destinationTopic = destinationTopic;
    }

    public Destination getDestination() throws JMSException {
        return jmsDestination;
    }

    public JmsConnection getConnection() {
        return con;
    }

    public JmsDestination setConnection(JmsConnection con) {
        if (MSystem.equals(this.con, con)) return this;
        this.con = con;
        try {
            reset();
            open();
        } catch (Exception e) {
            log().t(e);
        }
        return this;
    }

    @Override
    public Session getSession() {
        if (con == null) return null;
        return con.getSession();
    }

    @Override
    public synchronized void open() throws JMSException {
        if (isClosed()) throw new JMSException("destination closed");
        if (con == null || destination == null) return;
        if (jmsDestination == null || getSession() == null) {
            con.open();
            log().d("destination", destination);
            if (destinationTopic) jmsDestination = getSession().createTopic(destination);
            else jmsDestination = getSession().createQueue(destination);
        }
    }

    public String getName() {
        return (destinationTopic ? "/topic/" : "/queue/") + destination;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void reset() {
        jmsDestination = null;
    }

    public boolean isTopic() {
        return destinationTopic;
    }

    @Override
    public boolean isConnected() {
        return jmsDestination != null;
    }

    @Override
    public JmsDestination getJmsDestination() {
        return this;
    }
}
