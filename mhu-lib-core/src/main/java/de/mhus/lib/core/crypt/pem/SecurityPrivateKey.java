package de.mhus.lib.core.crypt.pem;

import java.security.PrivateKey;

import de.mhus.lib.core.util.SecureString;

public class SecurityPrivateKey implements PrivateKey {

    private static final long serialVersionUID = 1L;
    private String algorithm;
    private String format;
    private byte[] encoded;

    public SecurityPrivateKey(PemBlock pem, SecureString passphrase) throws Exception {
        if (!PemUtil.isPrivKey(pem))
            throw new SecurityException("Block is not a private key: " + pem.getName());
        algorithm = pem.getString(PemBlock.METHOD, "");
        format = pem.getString(PemBlock.FORMAT, "");
        encoded = pem.getBytesBlock();
        if (passphrase != null) {
            encoded = PemUtil.decrypt(pem, passphrase);
        }
    }
    
    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public byte[] getEncoded() {
        return encoded;
    }

}
