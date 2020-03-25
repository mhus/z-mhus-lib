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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TemporaryQueue;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.MLogUtil;

public class ClientJms extends JmsChannel implements MessageListener {

    private MessageProducer producer;

    private TemporaryQueue answerQueue;
    private MessageConsumer responseConsumer;

    private HashMap<String, Message> responses = null;
    private HashSet<String> allowedIds = new HashSet<>();
    private long timeout = MPeriod.MINUTE_IN_MILLISECOUNDS * 5;
    private long warnTimeout = MPeriod.MINUTE_IN_MILLISECOUNDS;
    private long broadcastTimeout = 5000; // 5 sec. to wait for answers by default

    private JmsInterceptor interceptorOut;
    private JmsInterceptor interceptorIn;

    public ClientJms(JmsDestination dest) {
        super(dest);
        try {
            ResourceNode<?> cfg = MJms.getConfig();
            timeout = cfg.getLong("answerTimeout", timeout);
            warnTimeout = cfg.getLong("answerWarnTimeout", warnTimeout);
            broadcastTimeout = cfg.getLong("broadcastTimeout", broadcastTimeout);
        } catch (Throwable t) {
            log().t(t);
        }
    }

    public void sendJmsOneWay(Message msg) throws JMSException {
        open();
        JmsContext context = new JmsContext(msg);
        prepareMessage(msg);
        if (interceptorOut != null) interceptorOut.prepare(context);
        log().d("sendJmsOneWay", dest, producer.getTimeToLive(), msg);
        try {
            producer.send(msg);
        } catch (IllegalStateException ise) {
            log().d("reconnect", getName(), ise.getMessage());
            producer = null;
            open();
            producer.send(msg);
        }
    }

    protected void prepareMessage(Message msg) throws JMSException {

        msg.setJMSMessageID(createMessageId());

        String config = MLogUtil.getTrailConfig();
        if (config != null) msg.setStringProperty(MConstants.LOG_MAPPER, config);
    }

    public Message sendJms(Message msg) throws JMSException {
        open();

        JmsContext context = new JmsContext(msg);
        prepareMessage(msg);
        String id = msg.getJMSMessageID();
        openAnswerQueue();
        msg.setJMSReplyTo(answerQueue);
        msg.setJMSCorrelationID(id);
        addAllowedId(id);
        if (interceptorOut != null) interceptorOut.prepare(context);
        try {
            log().d("sendJms", dest, producer.getTimeToLive(), msg);
            try {
                producer.send(msg);
            } catch (IllegalStateException ise) {
                log().d("reconnect", getName(), ise.getMessage());
                producer = null;
                open();
                openAnswerQueue();
                msg.setJMSReplyTo(answerQueue);
                producer.send(msg);
            }

            long start = System.currentTimeMillis();
            while (true) {
                try {
                    synchronized (this) {
                        this.wait(10000);
                    }
                } catch (InterruptedException e) {
                    log().t(e);
                }

                synchronized (responses) {
                    Message answer = responses.get(id);
                    if (answer != null) {
                        context.setAnswer(answer);
                        responses.remove(id);
                        log().d("sendJmsAnswer", dest, answer);
                        try {
                            if (interceptorIn != null) interceptorIn.answer(context);
                        } catch (Throwable t) {
                            log().d(t);
                        }
                        return answer;
                    }
                }

                long delta = System.currentTimeMillis() - start;
                if (delta > warnTimeout) log().w("long time waiting", dest, delta);

                if (delta > timeout) {
                    log().w("timeout", delta);
                    throw new JMSException("answer timeout " + dest);
                }
            }
        } finally {
            removeAllowedId(id);
        }
    }

    protected void addAllowedId(String id) {
        synchronized (responses) {
            responses.remove(id);
            allowedIds.add(id);
        }
    }

    protected void removeAllowedId(String id) {
        synchronized (responses) {
            responses.remove(id);
            allowedIds.remove(id);
        }
    }

    public Message[] sendJmsBroadcast(Message msg) throws JMSException {
        open();

        String id = createMessageId();
        msg.setJMSMessageID(id);
        openAnswerQueue();
        msg.setJMSReplyTo(answerQueue);
        msg.setJMSCorrelationID(id);
        addAllowedId(id);
        try {
            log().d("sendJmsBroadcast", dest, producer.getTimeToLive(), msg);
            try {
                producer.send(msg, deliveryMode, getPriority(), getTimeToLive());
            } catch (IllegalStateException ise) {
                log().d("reconnect", getName(), ise.getMessage());
                producer = null;
                open();
                openAnswerQueue();
                msg.setJMSReplyTo(answerQueue);
                producer.send(msg, deliveryMode, getPriority(), getTimeToLive());
            }

            long start = System.currentTimeMillis();
            LinkedList<Message> res = new LinkedList<>();
            while (true) {
                try {
                    synchronized (this) {
                        this.wait(1000);
                    }
                } catch (InterruptedException e) {
                    log().d(e);
                }

                synchronized (responses) {
                    Message answer = responses.get(id);
                    if (answer != null) {
                        responses.remove(id);
                        res.add(answer);
                    }
                }

                long delta = System.currentTimeMillis() - start;
                if (delta > broadcastTimeout) break;
            }

            log().d("sendJmsBroadcastAnswer", dest);
            log().t("sendJmsBroadcastAnswer", dest, res);
            return res.toArray(new Message[res.size()]);
        } catch (JMSException e) {
            reopen();
            throw e;
        } finally {
            removeAllowedId(id);
        }
    }

    @Override
    public synchronized void open() throws JMSException {
        if (isClosed()) throw new JMSException("client closed: " + getName());
        if (producer == null || getSession() == null) {
            dest.open();
            log().d("open", dest);
            producer = dest.getConnection().getSession().createProducer(dest.getDestination());
            if (timeout >= 0) producer.setTimeToLive(timeout);
            // reset answer queue
            try {
                if (answerQueue != null) answerQueue.delete();
            } catch (Throwable t) {
                log().t(t);
            }
            answerQueue = null;
        }
    }

    protected synchronized void openAnswerQueue() throws JMSException {
        if (isClosed()) throw new JMSException("client closed: " + getName());
        if (answerQueue == null || getSession() == null) {
            open();
            answerQueue = dest.getConnection().getSession().createTemporaryQueue();
            responseConsumer = dest.getConnection().getSession().createConsumer(answerQueue);
            responses = new HashMap<>();
            responseConsumer.setMessageListener(this);
        }
    }

    @Override
    public void onMessage(Message message) {
        if (message == null) return;
        try {
            synchronized (responses) {
                String id = message.getJMSCorrelationID();
                if (!allowedIds.contains(id)) return;
                responses.put(id, message);
            }
            synchronized (this) {
                this.notifyAll();
            }
        } catch (JMSException e) {
            log().d(e);
        }
    }

    @Override
    public void reset() {
        log().d("reset", dest);
        try {
            if (producer != null) producer.close();
        } catch (Throwable t) {
            log().d(t);
        }
        try {
            if (responseConsumer != null) responseConsumer.close();
        } catch (Throwable t) {
            log().d(t);
        }
        try {
            if (answerQueue != null) answerQueue.delete();
        } catch (Throwable t) {
            log().d(t);
        }
        producer = null;
        responseConsumer = null;
        answerQueue = null;
    }

    @Override
    public void doBeat() {
        // nothing to do
    }

    @Override
    public String getName() {
        return "openwire:/client" + dest.getName();
    }

    public long getBroadcastTimeout() {
        return broadcastTimeout;
    }

    public void setBroadcastTimeout(long broadcastTimeout) {
        this.broadcastTimeout = broadcastTimeout;
    }

    @Override
    public boolean isConnected() {
        return !(producer == null || getSession() == null);
    }

    public JmsInterceptor getInterceptorOut() {
        return interceptorOut;
    }

    public void setInterceptorOut(JmsInterceptor interceptorOut) {
        this.interceptorOut = interceptorOut;
    }

    public JmsInterceptor getInterceptorIn() {
        return interceptorIn;
    }

    public void setInterceptorIn(JmsInterceptor interceptorIn) {
        this.interceptorIn = interceptorIn;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
        if (timeout >= 0 && producer != null)
            try {
                producer.setTimeToLive(timeout);
            } catch (Exception e) {
            }
    }

    public long getWarnTimeout() {
        return warnTimeout;
    }

    public void setWarnTimeout(long warnTimeout) {
        this.warnTimeout = warnTimeout;
    }

    //	public MessageProducer getProducer() {
    //		return producer;
    //	}

}
