/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.crypt;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MBigMath;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;

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

	private static Log log = Log.getLog(MCrypt.class);
	
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
	public static String encodeWithSalt(AsyncKey key, String in) throws IOException {
		byte[] org = MString.toBytes(in);
		byte[] org2 = new byte[org.length+1];
		byte salt = MApi.lookup(MRandom.class).getByte();
		org2[0] = salt;
		for (int i = 0; i < org.length; i++)
			org2[i+1] = MMath.addRotate(org[i], salt);
		BigInteger[] enc = encodeBytes(key, org2);
		String b = MBigMath.toBase91(enc);
		return "A" + b;
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
		CipherEncodeAsync encoder = new CipherEncodeAsync(key,MApi.lookup(MRandom.class));
		for (int i = 0; i < in.length; i++)
			encoder.write(in[i]);
		encoder.close();
	    return encoder.toBigInteger();
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
	public static String decodeWithSalt(AsyncKey key, String in) throws IOException {
		BigInteger[] benc = MBigMath.fromBase91Array(in.substring(1));
		byte[] enc = MCrypt.decodeBytes(key, benc);
		if (in.charAt(0) == 'A') {
			byte[] enc2 = new byte[enc.length-1];
			byte salt = enc[0];
			for (int i = 0; i < enc2.length; i++)
				enc2[i] =  MMath.subRotate(enc[i+1], salt);
			return MString.toString(enc2);
		} else
			throw new IOException("Unknown salt algorithm");
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
		CipherDecodeAsync decoder = new CipherDecodeAsync(key);
		for (int i = 0; i < in.length; i++)
			decoder.write(in[i]);
		decoder.close();
	    return decoder.toBytes();
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
		return createCipherOutputStream(parent, passphrase, 3);
	}
	
	public static OutputStream createCipherOutputStream(OutputStream parent, String passphrase, int version) throws IOException {
		if (passphrase == null || passphrase.length() < 1)
			throw new IOException("passphrase not set");
		if (passphrase.length() < 4)
			throw new IOException("passphrase smaller then 4");
		byte[] p = MString.toBytes(passphrase);
		
		if (version < 2 || version > 3) throw new IOException("Cipher version unknown: " + version);
		
		parent.write('M');
		parent.write('C');
		parent.write('S');
		parent.write(version); // version
		
		MRandom random = MApi.lookup(MRandom.class);
		if (version == 2) {
			CipherBlockAdd cipher = new CipherBlockAdd(p);
			return new SaltOutputStream(new CipherOutputStream(parent, cipher), random, p.length-(random.getInt() % (p.length / 2)), true) ;
		}
		if (version == 3) {
			// extend passphrase
			byte pSalt = random.getByte();
			String md5 = md5(pSalt + passphrase);
			p = MString.toBytes(md5+passphrase);
			parent.write(MMath.unsignetByteToInt(pSalt));
			CipherBlockAdd cipher = new CipherBlockAdd(p);
			return new SaltOutputStream(new CipherOutputStream(parent, cipher), random, p.length-(random.getInt() % (p.length / 2)), true) ;
		}
		throw new IOException("Cipher version unknown: " + version);	
	}
	
	/**
	 * Create a stream to decode a data stream with a simple pass phrase using createCipherOutputStream.
	 * 
	 * @param parent
	 * @param passphrase
	 * @return new input stream
	 * @throws IOException
	 */
	public static InputStream createCipherInputStream(InputStream parent, String passphrase) throws IOException {
		if (passphrase == null || passphrase.length() < 1)
			throw new IOException("passphrase not set");
		if (passphrase.length() < 4)
			throw new IOException("passphrase smaller then 4");
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
		if (version == 2) {
			byte[] p = MString.toBytes(passphrase);
			CipherBlockAdd cipher = new CipherBlockAdd(p);
			return new SaltInputStream(new CipherInputStream(parent, cipher), true);
		} else
		if (version == 3) {
			byte pSalt = (byte) parent.read();
			String md5 = md5(pSalt + passphrase);
			byte[] p = MString.toBytes(md5+passphrase);
			CipherBlockAdd cipher = new CipherBlockAdd(p);
			return new SaltInputStream(new CipherInputStream(parent, cipher), true);
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

	/**
	 * Returns the maximum amount of bytes that can be encrypted at once.
	 * 
	 * @param modulus
	 * @return maximum byte length
	 */
	public static int getMaxLoad(BigInteger modulus) {
		return modulus.bitLength() / 8;
	}

	public static String md5(String real) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(real.getBytes());
			return MCast.toBinaryString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			log.t(e);
		}
		return null;
	}

	private static final int MAX_SPACE = 10;

	/**
	 * Encode the byte array synchronous using the pass phrase.
	 * 
	 * @param passphrase
	 * @param in
	 * @return encoded byte array
	 */
	public static byte[] encode(String passphrase, byte[] in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] pp = passphrase.getBytes();
		int ppPos = 0;
		MRandom random = MApi.lookup(MRandom.class);
		byte salt = random.getByte();
		
		// save salt
		byte o = MMath.addRotate(salt, pp[ppPos]);
		ppPos = (ppPos+1) % pp.length;
		out.write(o);
		
		for (int pos = 0; pos < in.length; pos++) {
			byte space = (byte) (random.getInt() % MAX_SPACE);
			// save space
			o = MMath.addRotate(space, pp[ppPos]);
			o = MMath.addRotate(o, salt);
			ppPos = (ppPos+1) % pp.length;
			out.write(o);
			// fill space
			for (int j = 0; j < space; j++)
				out.write(random.getByte());
			// write one byte
			o = MMath.addRotate(in[pos], pp[ppPos]);
			o = MMath.addRotate(o, salt);
			ppPos = (ppPos+1) % pp.length;
			out.write(o);
		}
		// one more trailing space
		byte space = (byte) (random.getInt() % MAX_SPACE);
		// save space
		o = MMath.addRotate(space, pp[ppPos]);
		o = MMath.addRotate(o, salt);
		ppPos = (ppPos+1) % pp.length;
		out.write(o);
		// fill space
		for (int j = 0; j < space; j++)
			out.write(random.getByte());

		return out.toByteArray();
	}

	/**
	 * Decode the byte array synchronous using the pass phrase.
	 * 
	 * @param passphrase
	 * @param in
	 * @return decoded byte array
	 */
	public static byte[] decode(String passphrase, byte[] in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] pp = passphrase.getBytes();
		int ppPos = 0;
		
		// read salt
		byte salt = MMath.subRotate(in[0], pp[ppPos]);
		ppPos = (ppPos+1) % pp.length;
		
		int mode = 0;
		byte space = 0;
		for (int pos = 1; pos < in.length; pos++) {
			if (mode == 0) {
				// read space length
				byte o = MMath.subRotate(in[pos], salt);
				space = MMath.subRotate(o, pp[ppPos]);
				ppPos = (ppPos+1) % pp.length;
				if (space == 0)
					mode = 2;
				else
					mode = 1;
			} else
			if (mode == 1) {
				space--;
				if (space <= 0)
					mode = 2;
			} else
			if (mode == 2) {
				byte o = MMath.subRotate(in[pos], salt);
				o = MMath.subRotate(o, pp[ppPos]);
				ppPos = (ppPos+1) % pp.length;
				out.write(o);
				mode = 0;
			}
		}
		
		return out.toByteArray();
	}
	
}
