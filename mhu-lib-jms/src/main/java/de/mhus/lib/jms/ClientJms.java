package de.mhus.lib.jms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TemporaryQueue;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.logging.Log;

public class ClientJms extends JmsChannel implements MessageListener {


	private MessageProducer producer;
	
	private TemporaryQueue answerQueue;
	private MessageConsumer responseConsumer;
	
	private HashMap<String, Message> responses = null;
	private HashSet<String> allowedIds = new HashSet<>();
	private long timeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 30;
	private long warnTimeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS;
	private long broadcastTimeout = 100;

	private JmsInterceptor interceptorOut;
	private JmsInterceptor interceptorIn;
	
	public ClientJms(JmsDestination dest) {
		super(dest);
	}
	
	public void sendJmsOneWay(Message msg) throws JMSException {
		open();
		msg.setJMSMessageID(createMessageId());
		if (interceptorOut != null)
			interceptorOut.prepare(msg);
		log().t("sendJmsOneWay",msg);
		producer.send(msg);
	}
	
	public Message sendJms(Message msg) throws JMSException {
		open();
		
		String id = createMessageId();
		msg.setJMSMessageID(id);
		openAnswerQueue();
		msg.setJMSReplyTo(answerQueue);
		msg.setJMSCorrelationID(id);
		addAllowedId(id);
		if (interceptorOut != null)
			interceptorOut.prepare(msg);
		try {
			log().t("sendJms",msg);
			producer.send(msg);
	
			long start = System.currentTimeMillis();
			while (true) {
				try {
					synchronized (this) {
						this.wait(10000);
					}
				} catch (InterruptedException e) {log().t(e);}
				
				synchronized (responses) {
					Message answer = responses.get(id);
					if (answer != null) {
						responses.remove(id);
						log().t("sendJmsAnswer",answer);
						return answer;
					}
				}
				
				long delta = System.currentTimeMillis() - start;
				if (delta > warnTimeout )
					log().w("long time waiting", delta);
				
				if (delta > timeout ) {
					log().w("timeout", delta);
					throw new JMSException("answer timeout");
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
			log().t("sendJmsBroadcast",msg);
			producer.send(msg);
	
			long start = System.currentTimeMillis();
			LinkedList<Message> res = new LinkedList<>();
			while (true) {
				try {
					synchronized (this) {
						this.wait(1000);
					}
				} catch (InterruptedException e) {log().t(e);}
				
				synchronized (responses) {
					Message answer = responses.get(id);
					if (answer != null) {
						responses.remove(id);
						res.add(answer);
					}
				}
				
				long delta = System.currentTimeMillis() - start;
				if (delta > broadcastTimeout )
					break;
			}
			
			log().t("sendJmsBroadcastAnswer",res);
			return res.toArray(new Message[res.size()]);
		} catch (JMSException e) {
			reset();
			throw e;
		} finally {
			removeAllowedId(id);
		}
	}
	
	@Override
	public synchronized void open() throws JMSException {
		if (isClosed()) throw new JMSException("client closed");
		if (producer == null || getSession() == null) {
			dest.open();
			log().i("open",dest);
			producer = dest.getConnection().getSession().createProducer(dest.getDestination());
		}
	}
	
	protected synchronized void openAnswerQueue() throws JMSException {
		if (isClosed()) throw new JMSException("client closed");
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
				if (interceptorIn != null)
					interceptorIn.answer(message);
				responses.put(id,message);
			}
			synchronized (this) {
				this.notifyAll();
			}
		} catch (JMSException e) {log().t(e);}
	}

	@Override
	public void reset() {
		log().i("reset",dest);
		try {
			producer.close();
		} catch (Throwable t) {log().t(t);}
		try {
			responseConsumer.close();
		} catch (Throwable t) {log().t(t);}
		producer = null;
		responseConsumer = null;
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

}
