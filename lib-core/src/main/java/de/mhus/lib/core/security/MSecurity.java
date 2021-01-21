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
package de.mhus.lib.core.security;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import de.mhus.lib.core.crypt.pem.PemBlock;
import de.mhus.lib.core.crypt.pem.PemBlockModel;

public class MSecurity {

    public static final String USER_CLASS = "UserPrincipal";
    public static final String GROUP_CLASS = "GroupPrincipal";
    public static final String ROLE_CLASS = "RolePrincipal";

    public static Principal getUser(Subject subject) {
        for (Principal p : subject.getPrincipals()) {
            if (p.getClass().getSimpleName().equals(USER_CLASS)) return p;
        }
        return null;
    }

    public static boolean hasGroup(Subject subject, String name) {
        if (name == null) return false;
        for (Principal p : subject.getPrincipals()) {
            if (p.getClass().getSimpleName().equals(GROUP_CLASS) && name.equals(p.getName()))
                return true;
        }
        return false;
    }

    public static boolean hasRole(Subject subject, String name) {
        if (name == null) return false;
        for (Principal p : subject.getPrincipals()) {
            if (p.getClass().getSimpleName().equals(ROLE_CLASS) && name.equals(p.getName()))
                return true;
        }
        return false;
    }

    public static List<Principal> getGroups(Subject subject) {
        LinkedList<Principal> out = new LinkedList<Principal>();
        for (Principal p : subject.getPrincipals()) {
            if (p.getClass().getSimpleName().equals(GROUP_CLASS)) out.add(p);
        }
        return out;
    }

    public static List<Principal> getRoles(Subject subject) {
        LinkedList<Principal> out = new LinkedList<Principal>();
        for (Principal p : subject.getPrincipals()) {
            if (p.getClass().getSimpleName().equals(ROLE_CLASS)) out.add(p);
        }
        return out;
    }

    public static void writeKey(File file, Key key, Map<String, Object> parameters)
            throws IOException {

        String type = "";
        if (key instanceof PrivateKey) type = " PRIVATE";
        else if (key instanceof PublicKey) type = " PUBLIC";
        type = key.getAlgorithm() + type + " KEY";
        PemBlockModel pem = new PemBlockModel(type, key.getEncoded());
        if (parameters != null) pem.putAll(parameters);
        pem.save(file);
    }

    public static String keyToString(Key key, Map<String, Object> parameters) throws IOException {

        String type = "";
        if (key instanceof PrivateKey) type = " PRIVATE";
        else if (key instanceof PublicKey) type = " PUBLIC";
        type = key.getAlgorithm() + type + " KEY";
        PemBlockModel pem = new PemBlockModel(type, key.getEncoded());
        if (parameters != null) pem.putAll(parameters);
        return pem.toString();
    }

    public static PublicKey readPublicKey(File file, String alorithm, String provider)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException,
                    NoSuchProviderException {

        PemBlockModel pem = PemBlockModel.load(file);
        byte[] encKey = pem.getBytesBlock();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance(alorithm, provider);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        return pubKey;
    }

    public static PublicKey readPublicKey(String textPem, String alorithm, String provider)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException,
                    NoSuchProviderException {

        PemBlockModel pem = PemBlockModel.loadFromString(textPem);
        byte[] encKey = pem.getBytesBlock();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance(alorithm, provider);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        return pubKey;
    }

    public static PrivateKey readPrivateKey(File file, String alorithm, String provider)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException,
                    NoSuchProviderException {

        PemBlockModel pem = PemBlockModel.load(file);
        byte[] encKey = pem.getBytesBlock();
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance(alorithm, provider);
        PrivateKey privKey = keyFactory.generatePrivate(encodedKeySpec);
        return privKey;
    }

    public static PrivateKey readPrivateKey(String textPem, String alorithm, String provider)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException,
                    NoSuchProviderException {

        PemBlockModel pem = PemBlockModel.loadFromString(textPem);
        byte[] encKey = pem.getBytesBlock();
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance(alorithm, provider);
        PrivateKey privKey = keyFactory.generatePrivate(encodedKeySpec);
        return privKey;
    }

    public static PemBlock readKey(File file)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException,
                    NoSuchProviderException {
        PemBlockModel pem = PemBlockModel.load(file);
        return pem;
    }
}
