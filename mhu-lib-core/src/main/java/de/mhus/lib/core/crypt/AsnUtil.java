package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Enumeration;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;

public class AsnUtil {

	// http://luca.ntop.org/Teaching/Appunti/asn1.html
	public static AsyncKey loadPrivateRsaKey(String key) throws IOException {
		
		int pos = key.indexOf("-----BEGIN RSA PRIVATE KEY-----\n");
		if (pos < 0) throw new IOException("begin of RSA Key not found");
		
		key = key.substring(pos + "-----BEGIN RSA PRIVATE KEY-----\n".length());
		
		pos = key.indexOf("-----END RSA PRIVATE KEY-----");
		if (pos < 0) throw new IOException("end of RSA Key not found");
		
		key = key.substring(0, pos);
		key = key.replace("\n", "").trim();
		byte[] asn = Base64.getDecoder().decode(key);
		
	    ASN1Sequence primitive = (ASN1Sequence) ASN1Sequence.fromByteArray(asn);
	    Enumeration<?> ex = primitive.getObjects();
	    BigInteger v = ((ASN1Integer) ex.nextElement()).getValue();

	    int version = v.intValue();
	    if (version != 0 && version != 1) {
	        throw new IOException("wrong version for RSA private key");
	    }
	    BigInteger modulus = ((ASN1Integer) ex.nextElement()).getValue();
	    BigInteger publicExponent = ((ASN1Integer) ex.nextElement()).getValue();
	    BigInteger privateExponent = ((ASN1Integer) ex.nextElement()).getValue();
	    BigInteger prime1 = ((ASN1Integer) ex.nextElement()).getValue();
	    BigInteger prime2 = ((ASN1Integer) ex.nextElement()).getValue();
	    BigInteger exponent1 = ((ASN1Integer) ex.nextElement()).getValue();
	    BigInteger exponent2 = ((ASN1Integer) ex.nextElement()).getValue();
	    BigInteger coefficient = ((ASN1Integer) ex.nextElement()).getValue();
		
		return new AsyncKey(modulus, publicExponent, privateExponent, prime1, prime2, exponent1, exponent2, coefficient);
	}
	
}
