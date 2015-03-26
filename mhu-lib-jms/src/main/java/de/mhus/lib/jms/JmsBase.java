package de.mhus.lib.jms;

import java.util.UUID;

import javax.jms.Session;

public abstract class JmsBase extends JmsObject {

	protected JmsDestination dest;

	public JmsBase(JmsDestination dest) {
		this.dest = dest;
		dest.getConnection().registerBase(this);
	}

	protected String createMessageId() {
		return UUID.randomUUID().toString();
	}

	public JmsDestination getDestination() {
		return dest;
	}

	@Override
	public Session getSession() {
		return dest.getSession();
	}

	@Override
	public void close() {
		try {
			dest.getConnection().unregisterBase(this);
		} catch (Throwable t) {log().t(t);}
		super.close();
	}

	public abstract void doBeat();
}
