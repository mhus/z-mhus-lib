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
