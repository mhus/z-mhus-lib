package de.mhus.lib.core.crypt;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Enumeration;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;

import de.mhus.lib.core.MBigMath;
import de.mhus.lib.core.MFile;

public class MCrypt {

	public static final BigInteger BYTE_SHIFT = new BigInteger("128");

	public static AsyncKey loadPrivateRsaKey(File file) throws IOException {
		String key = MFile.readFile(file);
		return loadPrivateRsaKey(key);
	}
	
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
		
/*	
	public static AsyncKey createKeyPair(BigInteger prime1, BigInteger prime2) {
	    // (D * E) % z = 1
	    BigInteger n = prime1.multiply(prime2);
	    BigInteger z = prime1.subtract(BigInteger.ONE).multiply(  prime2.subtract(BigInteger.ONE)   );
	    BigInteger e = MBigMath.computeDfromE(privateExponent, z);
	    BigInteger d = MBigMath.computeDfromE(publicExponent, z);

	    return new AsyncKey(n, publicExponent, privateExponent, prime1, prime2, e, d, null);
	}
*/	
	public static BigInteger encode(AsyncKey key, BigInteger in) throws IOException {
		if (in.signum() == -1) throw new IOException("Negative values are not allowed");
	    BigInteger encoded = MBigMath.binaryPow(in, key.getPublicExponent(), key.getModulus());
	    return encoded;
	}

	public static String encode(AsyncKey key, String in) throws IOException {
		byte[] org = in.getBytes();
		BigInteger[] enc = encodeBytes(key, org);
		String b = MBigMath.toBase91(enc);
		return b;
	}
	
	public static BigInteger[] encodeBytes(AsyncKey key, byte[] in) throws IOException {
		BigInteger[] out = new BigInteger[in.length];
		for (int i = 0; i < in.length; i++) {
			BigInteger c = new BigInteger(new byte[] {in[i]} ).add(BYTE_SHIFT);
			out[i] = encode(key, c);
		}
	    return out;
	}

	public static BigInteger[] encodeBytes(AsyncKey key, BigInteger[] in) throws IOException {
		BigInteger[] out = new BigInteger[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = encode(key, in[i]);
		}
	    return out;
	}
	
	public static BigInteger decode(AsyncKey key, BigInteger in) throws IOException {
		if (in.signum() == -1) throw new IOException("Negative values are not allowed");
	    BigInteger decoded = MBigMath.binaryPow(in, key.getPrivateExponent(), key.getModulus());
	    return decoded;
	}

	public static String decode(AsyncKey key, String in) throws IOException {
		BigInteger[] benc = MBigMath.fromBase91Array(in);
		byte[] enc = MCrypt.decodeBytes(key, benc);
		return new String(enc);
	}
	
	public static BigInteger[] decode(AsyncKey key, BigInteger[] in) throws IOException {
		BigInteger[] out = new BigInteger[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = decode(key, in[i]);
	    return out;
	}
	
	public static byte[] decodeBytes(AsyncKey key, BigInteger[] in) throws IOException {
		byte[] out = new byte[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = decode(key, in[i]).subtract(BYTE_SHIFT).byteValue();
	    return out;
	}
	
	public static CipherBlockRotate createRandomCipherBlockRotate(int size) {
		CipherBlockRotate out = new CipherBlockRotate(size);
		byte[] b = out.getBlock();
		for (int i = 0; i < b.length; i++)
			b[i] = (byte)(Math.random() * 256);
		return out;
	}
	
}
