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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQSession;

import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.logging.ITracer;
import de.mhus.lib.errors.MRuntimeException;
import io.opentracing.Scope;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.tag.Tags;

public abstract class ServerJms extends JmsChannel implements MessageListener {

    private static long usedThreads = 0;
    private static CfgLong maxThreadCount = new CfgLong(ServerJms.class, "maxThreadCount", -1);
    private static CfgLong maxThreadCountTimeout =
            new CfgLong(ServerJms.class, "maxThreadCountTimeout", 10000);
    private static CfgLong inactivityTimeout =
            new CfgLong(ServerJms.class, "inactivityTimeout", MPeriod.HOUR_IN_MILLISECOUNDS);
    
    private static CfgBoolean CFG_TRACE_ACTIVE = new CfgBoolean(ServerJms.class, "traceActive", false);
    
    public ServerJms(JmsDestination dest) {
        super(dest);
    }

    MessageConsumer consumer;

    private MessageProducer replyProducer;
    private JmsInterceptor interceptorIn;

    private JmsInterceptor interceptorOut;

    private boolean fork = true;
    private long lastActivity = System.currentTimeMillis();

    @Override
    public synchronized void open() throws JMSException {
        if (isClosed()) throw new JMSException("server closed");
        if (consumer == null || getSession() == null) {
            lastActivity = System.currentTimeMillis();
            dest.open();
            if (dest.getConnection() == null || dest.getConnection().getSession() == null)
                throw new JMSException("connection offline");
            log().i("consume", dest);
            consumer = dest.getConnection().getSession().createConsumer(dest.getDestination());
            consumer.setMessageListener(this);
            onOpen();
        }
    }

    /** The method is called after the open was successful. */
    protected void onOpen() {}

    /** The method is called after the reset operation. */
    protected void onReset() {}

    public synchronized void openAnswer() throws JMSException {
        if (replyProducer == null || getSession() == null) {
            open();
            replyProducer = dest.getSession().createProducer(null);
        }
    }

    @Override
    public void reset() {
        if (isClosed()) return;
        lastActivity = System.currentTimeMillis();
        log().i("reset", dest);
        try {
            if (consumer != null) consumer.close();
        } catch (Throwable t) {
            log().d(t);
        }
        try {
            if (replyProducer != null) replyProducer.close();
        } catch (Throwable t) {
            log().d(t);
        }
        consumer = null;
        replyProducer = null;
        onReset();
    }

    public abstract void receivedOneWay(Message msg) throws JMSException;

    public abstract Message received(Message msg) throws JMSException;

    protected void sendAnswer(JmsContext rpcContext) throws JMSException {
        Message msg = rpcContext.getMessage();
        Message answer = rpcContext.getAnswer();
        openAnswer();
        if (answer == null) {
            answer =
                    createErrorAnswer(
                            null); // other side is waiting for an answer - send a null text
            rpcContext.setAnswer(answer);
        }
        if (interceptorOut != null) interceptorOut.prepare(rpcContext);
        answer.setJMSMessageID(createMessageId());
        answer.setJMSCorrelationID(msg.getJMSCorrelationID());
        replyProducer.send(
                msg.getJMSReplyTo(), answer, deliveryMode, getPriority(), getTimeToLive());
    }

    @Override
    public void onMessage(final Message message) {

        if (fork) {

            long timeout = getMaxThreadCountTimeout();
            long mtc = getMaxThreadCount();
            while (mtc > 0 && getUsedThreads() > mtc) {

                /*
                "AT100[232] de.mhus.lib.jms.ServerJms$1" Id=232 in BLOCKED on lock=de....aaa.AccessApiImpl@48781daa
                     owned by AT92[224] de.mhus.lib.jms.ServerJms$1 Id=224
                     ...
                    at de.mhus.lib.karaf.jms.JmsDataChannelImpl.receivedOneWay(JmsDataChannelImpl.java:209)
                    at de.mhus.lib.karaf.jms.ChannelWrapper.receivedOneWay(ChannelWrapper.java:20)
                    at de.mhus.lib.jms.ServerJms.processMessage(ServerJms.java:182)
                    at de.mhus.lib.jms.ServerJms$1.run(ServerJms.java:120)
                    at de.mhus.lib.core.MThread$ThreadContainer.run(MThread.java:192)

                do not block jms driven threads !!! This will cause a deadlock

                				 */
                for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                    if (element.getClassName().equals(ServerJms.class.getCanonicalName())) {
                        log().w(
                                        "Too many JMS Threads ... ignore, it's a 'JMS to JMS' call",
                                        getUsedThreads());
                        break;
                    }
                }

                log().w("Too many JMS Threads ... wait!", getUsedThreads());
                MThread.sleep(100);
                timeout -= 100;
                if (timeout < 0) {
                    log().w("Too many JMS Threads ... timeout", getUsedThreads());
                    break;
                }
            }

            incrementUsedThreads();
            log().t(">>> usedThreads", getUsedThreads());

            new MThread(
                            new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        log().t("processMessage", message);
                                        processMessage(message);
                                    } finally {
                                        decrementUsedThreads();
                                        log().t("<<< usedThreads", getUsedThreads());
                                    }
                                }
                            },
                            getJmsDestination().getName())
                    .start();
        } else processMessage(message);
    }

    /** Overwrite this method to change default behavior. */
    protected void decrementUsedThreads() {
        usedThreads--;
    }

    /** Overwrite this method to change default behavior. */
    protected void incrementUsedThreads() {
        usedThreads++;
    }

    /**
     * Overwrite this method to change default behavior.
     *
     * @return current used threads
     */
    protected long getUsedThreads() {
        return usedThreads;
    }

    /**
     * Overwrite this method to change default behavior.
     *
     * @return
     */
    protected long getMaxThreadCount() {
        return maxThreadCount.value();
    }

    /**
     * Overwrite this method to change standard behavior.
     *
     * @return maxThreadCountTimeout.value()
     */
    protected long getMaxThreadCountTimeout() {
        return maxThreadCountTimeout.value();
    }

    public void processMessage(final Message message) {
        lastActivity = System.currentTimeMillis();

        Scope scope = null;
        try {
            if (message != null) {
                try {
                    SpanContext parentSpanCtx = ITracer.get().tracer().extract(Format.Builtin.TEXT_MAP, new TextMap() {

						@Override
						public Iterator<Entry<String, String>> iterator() {
							try {
								@SuppressWarnings("unchecked")
								final Enumeration<String> enu = message.getPropertyNames();
								return new Iterator<Entry<String,String>>() {
									@Override
									public boolean hasNext() {
										return enu.hasMoreElements();
									}
	
									@Override
									public Entry<String, String> next() {
										final String key = enu.nextElement();
										return new Entry<String, String>() {
	
											@Override
											public String getKey() {
												return key;
											}
	
											@Override
											public String getValue() {
												try {
													return message.getStringProperty(key);
												} catch (JMSException e) {
													throw new MRuntimeException(key,e);
												}
											}
	
											@Override
											public String setValue(String value) {
												return null;
											}
											
										};
									}
								};
							} catch (JMSException e) {
								throw new MRuntimeException(e);
							}
						}

						@Override
						public void put(String key, String value) {
							
						}
                    	
                    });
                    
                    if (parentSpanCtx == null) {
                    	scope = ITracer.get().start(getName(), CFG_TRACE_ACTIVE.value());
                    } else
                    if (parentSpanCtx != null) {
                    	scope = ITracer.get().tracer().buildSpan(getName()).asChildOf(parentSpanCtx).startActive(true);
                    }
                    
                    if (scope != null) {
                        Tags.SPAN_KIND.set(scope.span(), Tags.SPAN_KIND_SERVER);
                    }

                } catch (Throwable t) {
                }
            }
            log().d("received", dest, message);

            JmsContext context = new JmsContext(message);
            try {
                if (interceptorIn != null) {
                    interceptorIn.begin(context);
                }
            } catch (Throwable t) {
                log().w(t);
                try {
                    if (message.getJMSReplyTo() != null) {
                        TextMessage answer = createErrorAnswer(t);
                        log().d("errorAnswer", dest, answer);
                        context.setAnswer(answer);
                        sendAnswer(context);
                    }
                } catch (Throwable tt) {
                    log().w(tt);
                }
                return;
            }

            try {
                if (message.getJMSReplyTo() != null) {
                    Message answer = null;
                    try {
                        answer = received(message);
                    } catch (JMSException t) {
                        throw t;
                    } catch (Throwable t) {
                        log().w(t);
                        answer = createErrorAnswer(t);
                    }
                    log().d("receivedAnswer", dest, answer);
                    context.setAnswer(answer);
                    sendAnswer(context);
                } else {
                    log().d("receivedOneWay", dest, message);
                    receivedOneWay(message);
                }
            } catch (SendNoAnswerException e) {
                log().d("0", "Suppress send of an answer", dest);
                log().t(e);
            } catch (InvalidDestinationException t) {
                log().w("1", Thread.currentThread().getName(), t);
            } catch (JMSException t) {
                reset();
                log().w("2", Thread.currentThread().getName(), t);
            } catch (Throwable t) {
                log().w("3", Thread.currentThread().getName(), t);
            } finally {
                if (interceptorIn != null) {
                    interceptorIn.end(context);
                }
            }

        } finally {
            if (scope != null) {
            	scope.close();
            }
        }
    }

    protected TextMessage createErrorAnswer(Throwable t) throws JMSException {
        TextMessage ret = getSession().createTextMessage(null);
        if (t != null) ret.setStringProperty("_error", t.toString());
        return ret;
    }

    @Override
    public void doBeat() {
        if (isClosed()) return;
        log().d("beat", dest);
        try {
            Session session = getSession();
            if (session instanceof ActiveMQSession && ((ActiveMQSession) getSession()).isClosed()) {
                log().w("reconnect because session is closed", getName());
                consumer = null;
            }
            open(); // try to reopen and re-listen

            if (inactivityTimeout.value() > 0
                    && MPeriod.isTimeOut(lastActivity, inactivityTimeout.value())) reset();

        } catch (JMSException e) {
            log().d(e);
        }
    }

    @Override
    public String getName() {
        return "openwire:/server" + dest.getName();
    }

    @Override
    public boolean isConnected() {
        if (consumer != null) {
            try {
                Message msg = consumer.receiveNoWait();
                if (msg != null) onMessage(msg);
            } catch (JMSException e) {
                // ok: javax.jms.IllegalStateException: Cannot synchronously receive a message when
                // a MessageListener is set
                if (!e.toString()
                        .equals(
                                "javax.jms.IllegalStateException: Cannot synchronously receive a message when a MessageListener is set")) {
                    try {
                        consumer.close();
                        consumer = null;
                    } catch (JMSException e1) {
                        log().d(getName(), e1);
                    }
                }
            }
        }
        return !(consumer == null || getSession() == null);
    }

    @Override
    public void checkConnection() {
        try {
            open();
        } catch (JMSException e) {
            log().d(e);
        }
    }

    public JmsInterceptor getInterceptorIn() {
        return interceptorIn;
    }

    public void setInterceptorIn(JmsInterceptor interceptor) {
        this.interceptorIn = interceptor;
    }

    public JmsInterceptor getInterceptorOut() {
        return interceptorOut;
    }

    public void setInterceptorOut(JmsInterceptor interceptorOut) {
        this.interceptorOut = interceptorOut;
    }

    public boolean isFork() {
        return fork;
    }

    /**
     * Set to true if a incoming message should be handled by a worker thread (asynchrony) or set to
     * false if the incoming message should be processed by the JMS thread (synchrony). The default
     * is 'true' because a synchrony mode will block all message handlers and causes dead locks.
     *
     * @param fork
     */
    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public long getLastActivity() {
        return lastActivity;
    }
}
