package de.mhus.lib.core.mail;

public interface Mail {

	void send(MailTransport transport) throws Exception;
	
}
