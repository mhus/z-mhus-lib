package de.mhus.lib.core.mail;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(DefaultSendMail.class)
public interface MSendMail {

	void sendPlainMail(String from, String[] to, String[] cc, String[] bcc, String subject, String content) throws Exception;

	void sendHtmlMail(String from, String[] to, String[] cc, String[] bcc, String subject, String html, MailAttachment[] attachments) throws Exception;

}
