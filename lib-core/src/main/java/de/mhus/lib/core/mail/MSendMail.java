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

import de.mhus.lib.annotations.activator.DefaultImplementation;

/**
 * Facade to hide javax.mail packages. Using this interface do not need to add dependency to
 * javax.mail. Use Transport interface to access mail package.
 *
 * @author mikehummel
 */
@DefaultImplementation(DefaultSendMail.class)
public interface MSendMail {

    default void sendPlainMail(
            String from, String[] to, String[] cc, String[] bcc, String subject, String content)
            throws Exception {
        sendMail(
                new PlainTextMail()
                        .setFrom(from)
                        .setTo(to)
                        .setCc(cc)
                        .setBcc(bcc)
                        .setSubject(subject)
                        .setContent(content));
    }

    default void sendHtmlMail(
            String from,
            String[] to,
            String[] cc,
            String[] bcc,
            String subject,
            String html,
            MailAttachment[] attachments)
            throws Exception {
        sendMail(
                new HtmlMail()
                        .setFrom(from)
                        .setTo(to)
                        .setCc(cc)
                        .setBcc(bcc)
                        .setSubject(subject)
                        .setHtml(html)
                        .setAttachments(attachments));
    }

    void sendMail(Mail mail) throws Exception;

    MailTransport getMailTransport() throws Exception;
}
