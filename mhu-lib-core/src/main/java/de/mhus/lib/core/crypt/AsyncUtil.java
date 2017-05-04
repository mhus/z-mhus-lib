package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.math.BigInteger;

import de.mhus.lib.core.MBigMath;

public class AsyncUtil {

	public static final BigInteger BYTE_SHIFT = new BigInteger("128");

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
	
}
