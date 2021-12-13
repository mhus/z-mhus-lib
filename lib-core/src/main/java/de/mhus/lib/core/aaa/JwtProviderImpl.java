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
package de.mhus.lib.core.aaa;

import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MValidator;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.crypt.MBouncy;
import de.mhus.lib.core.crypt.pem.PemBlock;
import de.mhus.lib.core.crypt.pem.PemBlockModel;
import de.mhus.lib.core.keychain.DefaultEntry;
import de.mhus.lib.core.keychain.KeyEntry;
import de.mhus.lib.core.keychain.KeychainSource;
import de.mhus.lib.core.keychain.MKeychain;
import de.mhus.lib.core.keychain.MKeychainUtil;
import de.mhus.lib.core.keychain.MutableVaultSource;
import de.mhus.lib.core.security.MSecurity;
import de.mhus.lib.core.util.TimeoutMap;
import de.mhus.lib.errors.MRuntimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;

// https://github.com/jwtk/jjwt
public class JwtProviderImpl extends MLog implements JwtProvider {

    //    protected static final String ALGORITHM = "SHA256WITHECDSA";
    protected static final String ALGORITHM = "ECDSA";
    protected static final String PROVIDER = "BC";

    private CfgString CFG_SOURCE_SEC =
            new CfgString(JwtProvider.class, "privateSource", MKeychain.SOURCE_DEFAULT);
    private CfgString CFG_SOURCE_PUB =
            new CfgString(JwtProvider.class, "publicSource", MKeychain.SOURCE_DEFAULT);

    private Key jwtPrivateKey;
    private String jwtKeyId;
    private Map<String, PublicKey> publicKeyCache =
            Collections.synchronizedMap(new TimeoutMap<>(MPeriod.MINUTE_IN_MILLISECOUNDS * 10));
    private SigningKeyResolverAdapter jwtKeyResolver =
            new SigningKeyResolverAdapter() {
                @Override
                public Key resolveSigningKey(
                        @SuppressWarnings("rawtypes") JwsHeader jwsHeader, Claims claims) {
                    String kid = jwsHeader.getKeyId();
                    return getJwtPublicKeyById(kid);
                }
            };

    public JwtProviderImpl() {
        MBouncy.init();
    }

    public String createToken(
            String username,
            String issuer,
            BearerConfiguration configuration,
            Key privateKey,
            String keyId) {

        JwtBuilder builder = Jwts.builder().setSubject(username).signWith(privateKey);

        if (issuer != null) builder.setIssuer(issuer);
        else builder.setIssuer(getServerIdent());

        if (keyId != null) builder.setHeaderParam(JwsHeader.KEY_ID, keyId);

        if (configuration.isTimeout()) builder.setExpiration(new Date(configuration.getTTL()));
        return builder.compact();
    }

    @Override
    public JwsData readToken(String tokenStr) {
        return new JwsDataImpl(readToken(tokenStr, jwtKeyResolver));
    }

    public Jws<Claims> readToken(String tokenStr, SigningKeyResolver jwtKeyResolver) {
        try {
            Jws<Claims> jws =
                    Jwts.parserBuilder()
                            .setSigningKeyResolver(jwtKeyResolver)
                            .build()
                            .parseClaimsJws(tokenStr);

            // we can safely trust the JWT
            return jws;
        } catch (JwtException ex) {
            // we *cannot* use the JWT as intended by its creator
            log().d(ex);
            throw new AuthenticationException(ex);
//            throw new AccessDeniedException(ex);
        }
    }

    @Override
    public String createBearerToken(
            String username, String issuer, BearerConfiguration configuration)
            throws ShiroException {
        try {
            Key jwtPrivateKey = getJwtPrivateKey();
            return createToken(username, issuer, configuration, jwtPrivateKey, jwtKeyId);
        } catch (Throwable e) {
            log().d(username, e);
            throw new ShiroException(username, e);
        }
    }

    protected synchronized Key getJwtPrivateKey() throws ShiroException {

        try {
            MKeychain chain = MKeychainUtil.loadDefault();

            if (jwtPrivateKey == null) {
                String privName = "jws." + getServerIdent() + ".sec";
                KeyEntry entry = chain.getEntry(privName);
                if (entry != null) {
                    log().i("Load JWT key");
                    jwtKeyId =
                            PemBlockModel.loadFromString(entry.getValue().value())
                                    .getString(PemBlockModel.PUB_ID);
                    jwtPrivateKey =
                            MSecurity.readPrivateKey(entry.getValue().value(), ALGORITHM, PROVIDER);
                } else {
                    log().i("Create JWT keys");
                    // create key pair and save
                    KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
                    UUID pubId = UUID.randomUUID();
                    UUID privId = UUID.randomUUID();
                    {
                        MProperties parameters = new MProperties();
                        parameters.put(PemBlock.IDENT, privId);
                        parameters.put(PemBlock.PUB_ID, pubId);
                        parameters.put(PemBlock.ALGORITHM, ALGORITHM);
                        String pem = MSecurity.keyToString(keyPair.getPrivate(), parameters);

                        KeychainSource source = chain.getSource(CFG_SOURCE_SEC.value());
                        MutableVaultSource mutable = source.getEditable();
                        DefaultEntry e =
                                new DefaultEntry(
                                        privId,
                                        MKeychainUtil.getType(pem),
                                        privName,
                                        "JWT private key",
                                        pem);
                        mutable.addEntry(e);
                        mutable.doSave();
                    }
                    {
                        String pubName = "jws." + getServerIdent() + ".pub";

                        MProperties parameters = new MProperties();
                        parameters.put(PemBlock.IDENT, pubId);
                        parameters.put(PemBlock.PRIV_ID, privId);
                        parameters.put(PemBlock.ALGORITHM, ALGORITHM);
                        String pem = MSecurity.keyToString(keyPair.getPublic(), parameters);

                        KeychainSource source = chain.getSource(CFG_SOURCE_PUB.value());
                        MutableVaultSource mutable = source.getEditable();
                        DefaultEntry e =
                                new DefaultEntry(
                                        pubId,
                                        MKeychainUtil.getType(pem),
                                        pubName,
                                        "JWT public key",
                                        pem);
                        mutable.addEntry(e);
                        mutable.doSave();
                    }

                    jwtPrivateKey = keyPair.getPrivate();
                    jwtKeyId = pubId.toString();
                }
            }

            return jwtPrivateKey;

        } catch (Throwable t) {
            throw new ShiroException(t);
        }
    }

    protected String getServerIdent() {
        return MSystem.getHostname(); // TODO ?
    }

    protected Key getJwtPublicKeyById(String kid) {

        PublicKey key = publicKeyCache.get(kid);
        if (key != null) return key;

        try {
            MKeychain chain = MKeychainUtil.loadDefault();
            KeychainSource source = chain.getSource(CFG_SOURCE_PUB.value());
            KeyEntry e = null;
            if (MValidator.isUUID(kid)) e = source.getEntry(UUID.fromString(kid));
            else e = source.getEntry("jwt." + kid + ".pub");

            if (e == null) throw new MRuntimeException("Key unknown", kid);

            String pem = e.getValue().value();
            key = MSecurity.readPublicKey(pem, ALGORITHM, PROVIDER);
            publicKeyCache.put(kid, key);
            return key;
        } catch (Exception e) {
            throw new MRuntimeException(kid, e);
        }
    }

    private static class JwsDataImpl implements JwsData {

        private Jws<Claims> jws;

        public JwsDataImpl(Jws<Claims> jws) {
            this.jws = jws;
        }

        @Override
        public String getSubject() {
            return jws.getBody().getSubject();
        }
    }

    @Override
    public String getSubject(String tokenStr) {
        Jws<Claims> jws = readToken(tokenStr, jwtKeyResolver);
        return jws.getBody().getSubject();
    }

    public void clear() {
        jwtPrivateKey = null;
        jwtKeyId = null;
        publicKeyCache.clear();
    }
}
