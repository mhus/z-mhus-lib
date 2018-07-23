package de.mhus.lib.core.mail;

import java.util.Date;

import javax.mail.Message.RecipientType;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;

public class PlainTextMail extends MLog implements Mail {
	
	private String from;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String subject;
	private String content;

	public PlainTextMail setFrom(String from) {
		this.from = from;
		return this;
	}
	
	public PlainTextMail setTo(String[] to) {
		this.to = to;
		return this;
	}
	
	public PlainTextMail setCc(String[] cc) {
		this.cc = cc;
		return this;
	}

	public PlainTextMail setBcc(String[] bcc) {
		this.bcc = bcc;
		return this;
	}
	
	public PlainTextMail setSubject(String subject) {
		this.subject = subject;
		return this;
	}
	
	public PlainTextMail setContent(String content) {
		this.content = content;
		return this;
	}

	@Override
	public void send(MailTransport transport) throws Exception {
		
		InternetAddress[] toAddresses = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++)
			toAddresses[i] = new InternetAddress(to[i]);
		
		InternetAddress[] ccAddresses = null;
		if (cc != null && cc.length > 0) {
			ccAddresses = new InternetAddress[cc.length];
			for (int i = 0; i < cc.length; i++)
				ccAddresses[i] = new InternetAddress(cc[i]);
		}

		InternetAddress[] bccAddresses = null;
		if (bcc != null && bcc.length > 0) {
			bccAddresses = new InternetAddress[bcc.length];
			for (int i = 0; i < bcc.length; i++)
				bccAddresses[i] = new InternetAddress(bcc[i]);
		}

		MimeMessage msg = new MimeMessage(transport.getSession());
		msg.setFrom(MString.isSet(from) ? new InternetAddress(from) : transport.getFrom());
		msg.setRecipients(RecipientType.TO, toAddresses);
		if (ccAddresses != null)
			msg.setRecipients(RecipientType.CC, ccAddresses);
		if (bccAddresses != null)
			msg.setRecipients(RecipientType.BCC, bccAddresses);
		
		msg.setSubject(subject, "UTF-8");
        msg.setSentDate(new Date());
        msg.setText(content, "UTF-8");
		
        log().t(content);
		Transport.send(msg);
		
	}

	@Override
	public String toString() {
		return MSystem.toString(this, subject,"from",from,"to",to,"cc",cc,"bcc",bcc);
	}

}
