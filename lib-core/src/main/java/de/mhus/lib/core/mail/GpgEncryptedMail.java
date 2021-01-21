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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.crypt.MBouncy;

// http://www.docjar.org/src/api/org/bouncycastle/mail/smime/examples/CreateEncryptedMail.java
public class GpgEncryptedMail implements Mail {

    private String from;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String content;
    private KeyStore ks;

    public GpgEncryptedMail setFrom(String from) {
        this.from = from;
        return this;
    }

    public GpgEncryptedMail setTo(String... to) {
        this.to = to;
        return this;
    }

    public GpgEncryptedMail setCc(String... cc) {
        this.cc = cc;
        return this;
    }

    public GpgEncryptedMail setBcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }

    public GpgEncryptedMail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public GpgEncryptedMail setContent(String content) {
        this.content = content;
        return this;
    }

    public GpgEncryptedMail setKeyStore(KeyStore ks) {
        this.ks = ks;
        return this;
    }

    public GpgEncryptedMail openKeyStore(File file, String passphrase)
            throws NoSuchAlgorithmException, CertificateException, FileNotFoundException,
                    IOException, KeyStoreException, NoSuchProviderException {
        ks = KeyStore.getInstance("PKCS12", "BC");
        ks.load(new FileInputStream(file), passphrase.toCharArray());
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void send(MailTransport transport) throws Exception {

        MBouncy.init();

        InternetAddress[] toAddresses = new InternetAddress[to.length];
        for (int i = 0; i < to.length; i++) toAddresses[i] = new InternetAddress(to[i]);

        InternetAddress[] ccAddresses = null;
        if (cc != null && cc.length > 0) {
            ccAddresses = new InternetAddress[cc.length];
            for (int i = 0; i < cc.length; i++) ccAddresses[i] = new InternetAddress(cc[i]);
        }

        InternetAddress[] bccAddresses = null;
        if (bcc != null && bcc.length > 0) {
            bccAddresses = new InternetAddress[bcc.length];
            for (int i = 0; i < bcc.length; i++) bccAddresses[i] = new InternetAddress(bcc[i]);
        }

        Enumeration<?> e = ks.aliases();
        String keyAlias = null;

        while (e.hasMoreElements()) {
            String alias = (String) e.nextElement();

            if (ks.isKeyEntry(alias)) {
                keyAlias = alias;
            }
        }

        if (keyAlias == null) {
            System.err.println("can't find a private key!");
            System.exit(0);
        }

        Certificate[] chain = ks.getCertificateChain(keyAlias);

        //
        // create the generator for creating an smime/encrypted message
        //
        SMIMEEnvelopedGenerator gen = new SMIMEEnvelopedGenerator();

        gen.addKeyTransRecipient((X509Certificate) chain[0]);

        //
        // create a subject key id - this has to be done the same way as
        // it is done in the certificate associated with the private key
        // version 3 only.
        //
        /*
        MessageDigest           dig = MessageDigest.getInstance("SHA1", "BC");

        dig.update(cert.getPublicKey().getEncoded());

        gen.addKeyTransRecipient(cert.getPublicKey(), dig.digest());
        */

        //
        // create the base for our message
        //
        MimeBodyPart msg = new MimeBodyPart();

        msg.setText(content);

        MimeBodyPart mp = gen.generate(msg, SMIMEEnvelopedGenerator.RC2_CBC, "BC");
        //
        // Get a Session object and create the mail message
        //
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage body = new MimeMessage(session);
        body.setFrom(MString.isSet(from) ? new InternetAddress(from) : transport.getFrom());
        body.setRecipients(Message.RecipientType.TO, toAddresses);
        if (ccAddresses != null) body.setRecipients(RecipientType.CC, ccAddresses);
        if (bccAddresses != null) body.setRecipients(RecipientType.BCC, bccAddresses);
        body.setSubject(subject, MString.CHARSET_UTF_8);
        body.setContent(mp.getContent(), mp.getContentType());
        body.saveChanges();

        transport.send(body);
    }
}
