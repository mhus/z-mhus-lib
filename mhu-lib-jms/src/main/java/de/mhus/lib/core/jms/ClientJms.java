package de.mhus.lib.core.jms;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TemporaryQueue;

import de.mhus.lib.core.MTimeInterval;

public class ClientJms extends JmsBase implements MessageListener {


	private MessageProducer producer;
	
	private TemporaryQueue answerQueue;
	private MessageConsumer responseConsumer;
	
	private HashMap<String, Message> responses = null;
	private long timeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 30;
	private long warnTimeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS;
	
	public ClientJms(JmsDestination dest) {
		super(dest);
	}
	
	public void sendJmsOneWay(Message msg) throws JMSException {
		open();
		msg.setJMSMessageID(createMessageId());
		producer.send(msg);
	}
	
	public Message sendJms(Message msg) throws JMSException {
		open();
		
		String id = createMessageId();
		msg.setJMSMessageID(id);
		openAnswerQueue();
		msg.setJMSReplyTo(answerQueue);
		msg.setJMSCorrelationID(id);
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
				responses.put(message.getJMSCorrelationID(),message);
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
	}

	@Override
	public void doBeat() {
		// nothing to do
	}

}
