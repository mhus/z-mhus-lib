/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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

import java.io.IOException;
import java.util.Date;

import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;

public class HtmlMail extends MLog implements Mail {

    private String from;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String html;
    private MailAttachment[] attachments;

    public HtmlMail setFrom(String from) {
        this.from = from;
        return this;
    }

    public HtmlMail setTo(String... to) {
        this.to = to;
        return this;
    }

    public HtmlMail setCc(String... cc) {
        this.cc = cc;
        return this;
    }

    public HtmlMail setBcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }

    public HtmlMail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public HtmlMail setHtml(String html) {
        this.html = html;
        return this;
    }

    public HtmlMail setAttachments(MailAttachment... attachments) {
        this.attachments = attachments;
        return this;
    }

    @Override
    public void send(MailTransport transport) throws Exception {

        InternetAddress[] toAddresses = new InternetAddress[to.length];
        for (int i = 0; i < to.length; i++) toAddresses[i] = new InternetAddress(to[i]);

        InternetAddress[] ccAddresses = null;
        if (cc != null && cc.length > 0 && cc[0] != null) {
            ccAddresses = new InternetAddress[cc.length];
            for (int i = 0; i < cc.length; i++) ccAddresses[i] = new InternetAddress(cc[i]);
        }

        InternetAddress[] bccAddresses = null;
        if (bcc != null && bcc.length > 0 && bcc[0] != null) {
            bccAddresses = new InternetAddress[bcc.length];
            for (int i = 0; i < bcc.length; i++) bccAddresses[i] = new InternetAddress(bcc[i]);
        }

        MimeMessage msg = new MimeMessage(transport.getSession());
        msg.setFrom(MString.isSet(from) ? new InternetAddress(from) : transport.getFrom());
        msg.setRecipients(RecipientType.TO, toAddresses);
        if (ccAddresses != null) msg.setRecipients(RecipientType.CC, ccAddresses);
        if (bccAddresses != null) msg.setRecipients(RecipientType.BCC, bccAddresses);

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
        log().t(html);
        transport.send(msg);

        transport.cleanup(attachments);
    }

    @Override
    public String toString() {
        return MSystem.toString(
                this,
                subject,
                "from",
                from,
                "to",
                to,
                "cc",
                cc,
                "bcc",
                bcc,
                "attachments",
                attachments);
    }
}
