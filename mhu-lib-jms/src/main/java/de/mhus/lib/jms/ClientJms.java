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

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.logging.TrailLevelMapper;

public class ClientJms extends JmsChannel implements MessageListener {


	private MessageProducer producer;
	
	private TemporaryQueue answerQueue;
	private MessageConsumer responseConsumer;
	
	private HashMap<String, Message> responses = null;
	private HashSet<String> allowedIds = new HashSet<>();
	private long timeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 5;
	private long warnTimeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS;
	private long broadcastTimeout = 100;

	private JmsInterceptor interceptorOut;
	private JmsInterceptor interceptorIn;
	
	public ClientJms(JmsDestination dest) {
		super(dest);
		try {
			ResourceNode cfg = MJms.getConfig();
			timeout = cfg.getLong("answerTimeout", timeout);
			warnTimeout = cfg.getLong("answerWarnTimeout", warnTimeout);
			broadcastTimeout = cfg.getLong("broadcastTimeout", broadcastTimeout);
		} catch (Throwable t) {
			log().t(t);
		}
	}
	
	public void sendJmsOneWay(Message msg) throws JMSException {
		open();
		prepareMessage(msg);
		if (interceptorOut != null)
			interceptorOut.prepare(msg);
		log().d("sendJmsOneWay",dest,msg);
		producer.send(msg);
	}
	
	protected void prepareMessage(Message msg) throws JMSException {
		
		msg.setJMSMessageID(createMessageId());
		
		String config = MLogUtil.getTrailConfig();
		if (config != null)
			msg.setStringProperty(MConstants.LOG_MAPPER, config);
	}

	public Message sendJms(Message msg) throws JMSException {
		open();
		
		prepareMessage(msg);
		String id = msg.getJMSMessageID();
		openAnswerQueue();
		msg.setJMSReplyTo(answerQueue);
		msg.setJMSCorrelationID(id);
		addAllowedId(id);
		if (interceptorOut != null)
			interceptorOut.prepare(msg);
		try {
			log().d("sendJms",dest,msg);
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
						log().d("sendJmsAnswer",dest,answer);
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
			log().d("sendJmsBroadcast",dest,msg);
			producer.send(msg, deliveryMode, getPriority(), getTimeToLive());
	
			long start = System.currentTimeMillis();
			LinkedList<Message> res = new LinkedList<>();
			while (true) {
				try {
					synchronized (this) {
						this.wait(1000);
					}
				} catch (InterruptedException e) {log().d(e);}
				
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
			
			log().d("sendJmsBroadcastAnswer",dest,res);
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
			log().d("open",dest);
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
		} catch (JMSException e) {log().d(e);}
	}

	@Override
	public void reset() {
		log().d("reset",dest);
		try {
			producer.close();
		} catch (Throwable t) {log().d(t);}
		try {
			responseConsumer.close();
		} catch (Throwable t) {log().d(t);}
		try {
			if (answerQueue != null)
				answerQueue.delete();
		} catch (Throwable t) {log().d(t);}
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
