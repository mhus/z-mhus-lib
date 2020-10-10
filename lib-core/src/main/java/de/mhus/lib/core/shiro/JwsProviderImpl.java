package de.mhus.lib.core.shiro;

import java.io.File;
import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.ShiroException;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.cfg.CfgFile;
import de.mhus.lib.core.crypt.pem.PemBlock;
import de.mhus.lib.core.security.MSecurity;
import de.mhus.lib.core.util.TimeoutMap;
import de.mhus.lib.errors.AccessDeniedException;
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

public class JwsProviderImpl extends MLog implements JwsProvider {

//    protected static final String ALGORITHM = "SHA256WITHECDSA";
    protected static final String ALGORITHM = "ECDSA";
    protected static final String PROVIDER = "BC";
    
    private CfgFile CFG_KEYS_DIR = new CfgFile(JwsProvider.class, "keysDirectory",  MApi.getFile(MApi.SCOPE.DATA, "keys") );
    private Key jwtPrivateKey;
    private String jwtKeyId;
    private Map<String, PublicKey > publicKeyCache = Collections.synchronizedMap(new TimeoutMap<>(MPeriod.MINUTE_IN_MILLISECOUNDS * 10));
    private SigningKeyResolverAdapter jwtKeyResolver = new SigningKeyResolverAdapter() {
        @Override
        public Key resolveSigningKey(@SuppressWarnings("rawtypes") JwsHeader jwsHeader, Claims claims) {
            String kid = jwsHeader.getKeyId();
            File f = new File(CFG_KEYS_DIR.value(), MFile.normalize(kid) + ".pub");
            if (!f.exists()) throw new MRuntimeException("key not found",kid);
            PublicKey key = publicKeyCache.get(kid);
            if (key != null) return key;
            try {
                key = MSecurity.readPublicKey(f, ALGORITHM, PROVIDER);
                publicKeyCache.put(kid, key);
                return key;
            } catch (Exception e) {
                throw new MRuntimeException(kid,e);
            }
        }
    };

    public String createToken(String username, BearerConfiguration configuration, Key privateKey, String keyId) {

        JwtBuilder builder = Jwts.builder().setSubject(username).signWith(privateKey);

        if (keyId != null)
            builder.setHeaderParam(JwsHeader.KEY_ID, keyId);

        if (configuration.isTimeout())
            builder.setExpiration(new Date(configuration.getTTL()));
        return builder.compact();
    }

    @Override
    public JwsData readToken(String tokenStr) {
        return new JwsDataImpl(readToken(tokenStr, jwtKeyResolver));
    }

    public Jws<Claims> readToken(String tokenStr, SigningKeyResolver jwtKeyResolver) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKeyResolver(jwtKeyResolver)
                    .build()
                    .parseClaimsJws(tokenStr);
            
            // we can safely trust the JWT
             return jws;
        } catch (JwtException ex) {
            // we *cannot* use the JWT as intended by its creator
            throw new AccessDeniedException(ex);
        }
    }

    @Override
    public String createBearerToken(String username, BearerConfiguration configuration) throws ShiroException {
        try {
            Key jwtPrivateKey = getJwtPrivateKey();
            return createToken(username, configuration, jwtPrivateKey, jwtKeyId);
        } catch (Throwable e) {
            log().d(username, e);
            throw new ShiroException(username,e);
        }
    }

    public Key getJwtPrivateKey() throws ShiroException {
        try {
            if (jwtPrivateKey == null) {
                File f = new File(CFG_KEYS_DIR.value(), "key.sec");
                if (f.exists() && f.isFile()) {
                    log().i("Load JWT keys",f);
                    jwtKeyId = MSecurity.readKey(f).getString(PemBlock.IDENT);
                    jwtPrivateKey = MSecurity.readPrivateKey(f, ALGORITHM, PROVIDER);
                } else {
                    log().i("Create JWT keys",f);
                    // create key pair and save
                    KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
                    jwtPrivateKey = keyPair.getPrivate();
                    MFile.mkParentDir(f);
                    jwtKeyId = UUID.randomUUID().toString();
                    MSecurity.writeKey(f, jwtPrivateKey, jwtKeyId);
                    PublicKey jwtPublicKey = keyPair.getPublic();
                    File fp = new File(CFG_KEYS_DIR.value(), jwtKeyId + ".pub");
                    MSecurity.writeKey(fp, jwtPublicKey, jwtKeyId);
                }
            }

            return jwtPrivateKey;

        } catch (Throwable t) {
            throw new ShiroException(t);
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

}
