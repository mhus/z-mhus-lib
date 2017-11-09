package de.mhus.lib.core.crypt;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MBigMath;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MString;

/**
 * This tool is implementing functions to work with encryption and obfuscation to protect data.
 * The algorithm is implemented separate to the usual java.security package. If you want to
 * use the save and proven java implementation do not use this tool. With this tool you do not need 
 * any high security patch for your java JRE.
 * 
 * You need to provide the bouncycastle bcprov-jdk15on library to read keys from ASN1 encoded files.
 * 
 * @author mikehummel
 *
 */
public class MCrypt {

	public static final BigInteger BYTE_SHIFT = new BigInteger("128");

	/**
	 * Load a private key from file.
	 * 
	 * @param file
	 * @return the key object
	 * @throws IOException
	 */
	public static AsyncKey loadPrivateRsaKey(File file) throws IOException {
		String key = MFile.readFile(file);
		return loadPrivateRsaKey(key);
	}

	/**
	 * Load a public key from file.
	 * 
	 * @param file
	 * @return the key object
	 * @throws IOException
	 */
	public static AsyncKey loadPublicRsaKey(File file) throws IOException {
		String key = MFile.readFile(file);
		return loadPublicRsaKey(key);
	}

	/**
	 * Load a RSA private key into a AsyncKey object.
	 * 
	 * @param key key as ASN1 encoded string containing "-----BEGIN RSA PRIVATE KEY-----"
	 * 
	 * @return Corresponding key object
	 * @throws IOException If the key start or stop token was not found
	 */
	public static AsyncKey loadPrivateRsaKey(String key) throws IOException {
		return Asn1Util.loadPrivateRsaKey(key);
	}	
	
	/**
	 * Load a RSA public key into a AsyncKey object.
	 * 
	 * @param key key as ASN1 encoded string containing "-----BEGIN RSA PUBLIC KEY-----"
	 * 
	 * @return Corresponding key object
	 * @throws IOException If the key start or stop token was not found
	 */
	public static AsyncKey loadPublicRsaKey(String key) throws IOException {
		return Asn1Util.loadPublicRsaKey(key);
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
	/**
	 * Encode data using a RSA like algorithm. It's not using the java implementation.
	 * 
	 * @param key public key
	 * @param in clear data
	 * @return encoded data
	 * @throws IOException
	 */
	public static BigInteger encode(AsyncKey key, BigInteger in) throws IOException {
		if (in.signum() == -1) throw new IOException("Negative values are not allowed");
	    BigInteger encoded = MBigMath.binaryPow(in, key.getPublicExponent(), key.getModulus());
	    return encoded;
	}

	/**
	 * Encode data using a RSA like algorithm. It's not using the java implementation.
	 * 
	 * @param key public key
	 * @param in clear data
	 * @return encoded and Base91 encoded string
	 * @throws IOException
	 */
	public static String encode(AsyncKey key, String in) throws IOException {
		byte[] org = MString.toBytes(in);
		BigInteger[] enc = encodeBytes(key, org);
		String b = MBigMath.toBase91(enc);
		return b;
	}
	
	/**
	 * Encode data using a RSA like algorithm. It's not using the java implementation.
	 * 
	 * @param key public key
	 * @param in clear data
	 * @return encoded data
	 * @throws IOException
	 */
	public static BigInteger[] encodeBytes(AsyncKey key, byte[] in) throws IOException {
		BigInteger[] out = new BigInteger[in.length];
		for (int i = 0; i < in.length; i++) {
			BigInteger c = new BigInteger(new byte[] {in[i]} ).add(BYTE_SHIFT);
			out[i] = encode(key, c);
		}
	    return out;
	}

	/**
	 * Encode data using a RSA like algorithm. It's not using the java implementation.
	 * 
	 * @param key public key
	 * @param in clear data
	 * @return encoded data
	 * @throws IOException
	 */
	public static BigInteger[] encodeBytes(AsyncKey key, BigInteger[] in) throws IOException {
		BigInteger[] out = new BigInteger[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = encode(key, in[i]);
		}
	    return out;
	}
	
	/**
	 * Decode one byte using the private key.
	 * 
	 * @param key private key
	 * @param in encoded byte
	 * @return decoded byte
	 * @throws IOException
	 */
	public static BigInteger decode(AsyncKey key, BigInteger in) throws IOException {
		if (in.signum() == -1) throw new IOException("Negative values not allowed");
	    BigInteger decoded = MBigMath.binaryPow(in, key.getPrivateExponent(), key.getModulus());
	    return decoded;
	}
	
	/**
	 * Decode the data using Base91 byte encoding and the private key from 'key' using a RSA like algorithm. It's not the
	 * java implementation used.
	 * 
	 * @param key private key
	 * @param in the encoded data presentation
	 * @return the decoded string
	 * @throws IOException
	 */
	public static String decode(AsyncKey key, String in) throws IOException {
		BigInteger[] benc = MBigMath.fromBase91Array(in);
		byte[] enc = MCrypt.decodeBytes(key, benc);
		return MString.toString(enc);
	}
	
	/**
	 * Decode the data using the private key from 'key' using a RSA like algorithm. It's not the
	 * java implementation used.
	 * 
	 * @param key private key
	 * @param in encoded data
	 * @return decoded array of data
	 * @throws IOException
	 */
	public static BigInteger[] decode(AsyncKey key, BigInteger[] in) throws IOException {
		BigInteger[] out = new BigInteger[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = decode(key, in[i]);
	    return out;
	}
	
	/**
	 * Decode data using the private key from 'key' using a RSA like algorithm. It's not the
	 * java implementation used.
	 * 
	 * @param key private key
	 * @param in encoded data
	 * @return decoded array of data.
	 * @throws IOException
	 */
	public static byte[] decodeBytes(AsyncKey key, BigInteger[] in) throws IOException {
		byte[] out = new byte[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = decode(key, in[i]).subtract(BYTE_SHIFT).byteValue();
	    return out;
	}
	
	/**
	 * Create a random block using the MRandom service.
	 * 
	 * @param size
	 * @return CipherBlockRotate
	 */
	public static CipherBlockRotate createRandomCipherBlockRotate(int size) {
		CipherBlockRotate out = new CipherBlockRotate(size);
		byte[] b = out.getBlock();
		for (int i = 0; i < b.length; i++)
			b[i] = MApi.lookup(MRandom.class).getByte();
		return out;
	}
	
	/**
	 * Create a output stream automatically encode the stream with the pass phrase.
	 * 
	 * @param parent
	 * @param passphrase
	 * @return OutputStream
	 * @throws IOException
	 */
	public static OutputStream createCipherOutputStream(OutputStream parent, String passphrase) throws IOException {
		if (passphrase == null || passphrase.length() < 1)
			throw new IOException("passphrase not set");
		byte salt = MApi.lookup(MRandom.class).getByte();
		byte[] p = MString.toBytes(passphrase);
		
		parent.write('M');
		parent.write('C');
		parent.write('S');
		parent.write(1); // version
		parent.write(MMath.addRotate(salt,p[0]));
		
		for (int i = 0; i < p.length; i++)
			p[i] = MMath.addRotate(p[i],salt);
		
		CipherBlockAdd cipher = new CipherBlockAdd(p);
		return new CipherOutputStream(parent, cipher);
	}
	
	/**
	 * Create a stream to decode a data stream with a simple pass phrase using createCipherOutputStream.
	 * 
	 * @param parent
	 * @param passphrase
	 * @return
	 * @throws IOException
	 */
	public static InputStream createCipherInputStream(InputStream parent, String passphrase) throws IOException {
		if (passphrase == null || passphrase.length() < 1)
			throw new IOException("passphrase not set");
		if (parent.read() != 'M') throw new IOException("not a crypt stream header");
		if (parent.read() != 'C') throw new IOException("not a crypt stream header");
		if (parent.read() != 'S') throw new IOException("not a crypt stream header");
		int version = parent.read();
		if (version == 1) {
			int iSalt = parent.read();
			if (iSalt < 0) throw new EOFException();
			byte[] p = MString.toBytes(passphrase);
			byte salt = MMath.subRotate( (byte)iSalt, p[0] );
	
			for (int i = 0; i < p.length; i++)
				p[i] = MMath.addRotate(p[i],salt);
			
			CipherBlockAdd cipher = new CipherBlockAdd(p);
			return new CipherInputStream(parent, cipher);
		} else
			throw new IOException("unsupported crypt stream version: " + version);
	}
	
	/**
	 * Only obfuscate the byte array. The obfuscation is used to
	 * confuse the reader but not to secure the data.
	 * 
	 * TODO: Create more complex algorithm
	 * 
	 * @param in
	 * @return an obfuscated string
	 */
	public static byte[] obfuscate(byte[] in) {
		if (in == null) return null;
		if (in.length < 1) return in;
		byte[] out = new byte[in.length+1];
		byte salt = MApi.lookup(MRandom.class).getByte();
		out[0] = salt;
		for (int i = 0; i < in.length; i++)
			out[i+1] = MMath.addRotate(in[i], salt);
		return out;
	}

	/**
	 * Decode obfuscated byte array.
	 * 
	 * @param in
	 * @return original byte array
	 */
	public static byte[] unobfuscate(byte[] in) {
		if (in == null) return null;
		if (in.length < 2) return in;
		byte[] out = new byte[in.length-1];
		byte salt = in[0];
		for (int i = 1; i < in.length; i++)
			out[i-1] = MMath.subRotate(in[i], salt);
		return out;
	}

}
