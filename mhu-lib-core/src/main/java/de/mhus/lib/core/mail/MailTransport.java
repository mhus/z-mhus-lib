package de.mhus.lib.core.mail;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

public interface MailTransport {

	Session getSession() throws Exception;

	Address getFrom() throws AddressException;

	void send(MimeMessage msg) throws Exception;

	void cleanup(MailAttachment[] attachments);

	String getConnectInfo();

}
