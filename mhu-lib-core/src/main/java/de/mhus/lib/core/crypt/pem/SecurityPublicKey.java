package de.mhus.lib.core.crypt.pem;

import java.security.PublicKey;

public class SecurityPublicKey implements PublicKey {

    private static final long serialVersionUID = 1L;
    private String algorithm;
    private String format;
    private byte[] encoded;

    public SecurityPublicKey(PemBlock pem) {
        if (!PemUtil.isPubKey(pem))
            throw new SecurityException("Block is not a public key: " + pem.getName());
        algorithm = pem.getString(PemBlock.METHOD, "");
        format = pem.getString(PemBlock.FORMAT, "");
        encoded = pem.getBytesBlock();
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
