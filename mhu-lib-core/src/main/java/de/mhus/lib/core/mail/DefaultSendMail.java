/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.mail;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.cfg.CfgString;

/*
mhu config:

<de.mhus.lib.core.mail.MSendMail
  user=""
  password=""
  host="localhost"
  port="587"
  tls="true"
  from=""
  connection.timeout="60000"
/>

 */
public class DefaultSendMail extends MLog implements MSendMail {

	private static CfgString CFG_MAIL_USER = new CfgString(MSendMail.class, "user", "");
	private static CfgString CFG_MAIL_PASSWORD = new CfgString(MSendMail.class, "password", "");
	private static CfgString CFG_HOST = new CfgString(MSendMail.class, "host", "localhost");
	private static CfgString CFG_PORT = new CfgString(MSendMail.class, "port", "587");
	private static CfgBoolean CFG_TLS = new CfgBoolean(MSendMail.class, "tls", true);
	private static CfgString CFG_FROM = new CfgString(MSendMail.class, "from", "");
	private static CfgLong CFG_CONNECTION_TIMEOUT = new CfgLong(MSendMail.class, "connection.timeout", 60 * 1000);
	
	private Transport transport;
	private long lastMailTransport;
	private Session session;

	protected void connect() throws MessagingException {
		
		synchronized (this) {
	        if (transport != null) {
	        	if (MTimeInterval.isTimeOut(lastMailTransport, CFG_CONNECTION_TIMEOUT.value()) ||  !transport.isConnected()) {
	        		reset();
	        	}
	        }
	
	        
			if (session == null || transport == null) {
				
				Properties properties = new Properties();
				properties.put("mail.transport.protocol", "smtp");
	            properties.put("mail.smtp.host", CFG_HOST.value());
	            properties.put("mail.smtp.port", CFG_PORT.value());
	            if (MString.isSet(CFG_MAIL_USER.value())) {
	            	properties.put("mail.smtp.auth", "true");
	            	properties.put("mail.user", CFG_MAIL_USER.value());
	            	properties.put("mail.password", MPassword.decode(CFG_MAIL_PASSWORD.value()));
	            }
	            if (CFG_TLS.value()) {
	            	properties.put("mail.smtp.starttls.enable", true);
	            	properties.put("mail.smtp.ssl.trust", CFG_HOST.value());
	            }

				Authenticator auth = null;
				if (MString.isSet(CFG_MAIL_USER.value())) {
			        auth = new Authenticator() {
			            @Override
						public PasswordAuthentication getPasswordAuthentication() {
			                return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
			            }
			        };
				}
				
	        	session = Session.getInstance(properties, auth);
	        	transport = session.getTransport();
	        	transport.connect();
	        }
		}
	}

	public void reset() {
		synchronized (this) {
			if (transport != null)
				try {
					transport.close();
				} catch (MessagingException e) {
				}
			transport = null;
			session = null;
			lastMailTransport = 0;
		}
	}

	@Override
	public void sendPlainMail(String from, String[] to, String[] cc, String[] bcc, String subject, String content) throws Exception {
		
		connect();
		if (from == null) from = CFG_FROM.value();
		
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

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(RecipientType.TO, toAddresses);
		if (ccAddresses != null)
			msg.setRecipients(RecipientType.CC, ccAddresses);
		if (bccAddresses != null)
			msg.setRecipients(RecipientType.BCC, bccAddresses);
		
		msg.setSubject(subject, "UTF-8");
        msg.setSentDate(new Date());
        msg.setText(content, "UTF-8");
		
		Transport.send(msg);
		
	}

	@Override
	public void sendHtmlMail(String from, String[] to, String[] cc, String[] bcc, String subject, String html,
	        MailAttachment[] attachments) throws Exception {

		connect();
		if (from == null) from = CFG_FROM.value();
		
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

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(RecipientType.TO, toAddresses);
		if (ccAddresses != null)
			msg.setRecipients(RecipientType.CC, ccAddresses);
		if (bccAddresses != null)
			msg.setRecipients(RecipientType.BCC, bccAddresses);
		
		msg.setSubject(subject, "UTF-8");
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
        messageBodyPart.setHeader("Content-Transfer-Encoding", "quoted-printable");
        messageBodyPart.setContent(html, "text/html; charset=utf-8");
 
        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
 
        // adds attachments
        if (attachments != null)
            for (MailAttachment attachment : attachments) {
            	if (attachment.getFile().exists() && attachment.getFile().isFile()) {
	                MimeBodyPart attachPart = new MimeBodyPart();
	                try {
	                	attachPart.setFileName(attachment.getName());
	                    attachPart.attachFile(attachment.getFile());
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                }
	 
	                multipart.addBodyPart(attachPart);
            	}
            }
        
        
        msg.setContent(multipart);
        
        // send
		Transport.send(msg);
		
		// delete attachments
        if (attachments != null)
            for (MailAttachment attachment : attachments)
            	if (attachment.isDeleteAfterSent()) 
            		try {
            			attachment.getFile().delete();
            		} catch (Throwable t) {}

	}

}
