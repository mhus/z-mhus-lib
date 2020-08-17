/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MString;
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
    private static CfgLong CFG_CONNECTION_TIMEOUT =
            new CfgLong(MSendMail.class, "connection.timeout", 60 * 1000);

    private Transport transport;
    private long lastMailTransport;
    private Session session;
    private DefaultMailTransport mailTransport;

    protected void connect() throws MessagingException {

        synchronized (this) {
            if (transport != null) {
                if (MPeriod.isTimeOut(lastMailTransport, CFG_CONNECTION_TIMEOUT.value())
                        || !transport.isConnected()) {
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
                    auth =
                            new Authenticator() {
                                @Override
                                public PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(
                                            properties.getProperty("mail.user"),
                                            properties.getProperty("mail.password"));
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
    public void sendMail(Mail mail) throws Exception {
        log().d("mail", mail);
        mail.send(getMailTransport());
    }

    @Override
    public MailTransport getMailTransport() throws Exception {
        if (mailTransport == null) mailTransport = new DefaultMailTransport();
        return mailTransport;
    }

    private class DefaultMailTransport implements MailTransport {

        @Override
        public Session getSession() throws Exception {
            connect();
            return session;
        }

        @Override
        public Address getFrom() throws AddressException {
            return new InternetAddress(CFG_FROM.value());
        }

        @Override
        public void send(MimeMessage msg) throws Exception {
            connect();
            Transport.send(msg);
        }

        @Override
        public void cleanup(MailAttachment[] attachments) {
            // delete attachments
            if (attachments != null)
                for (MailAttachment attachment : attachments)
                    if (attachment != null && attachment.isDeleteAfterSent())
                        try {
                            attachment.getFile().delete();
                        } catch (Throwable t) {
                        }
        }

        @Override
        public String getConnectInfo() {
            return (CFG_TLS.value() ? "tls:" : "smtp:")
                    + "//"
                    + (MString.isSet(CFG_MAIL_USER.value()) ? CFG_MAIL_USER.value() + "@" : "")
                    + CFG_HOST.value()
                    + ":"
                    + CFG_PORT.value();
        }
    }
}
