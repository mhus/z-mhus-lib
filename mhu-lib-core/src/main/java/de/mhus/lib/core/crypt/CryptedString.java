package de.mhus.lib.core.crypt;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;

public class CryptedString implements Externalizable {
	
	private byte[] data;
	private byte[] rand;
	private int length;
	private String pubKeyMd5;
	
	public CryptedString() {}
	
	public CryptedString(KeyPair key, String secret) {
		try {
			if (secret == null) {
				data = null;
				length = 0;
			} else {
				length = secret.length();
				byte[] r = BouncyUtil.createRand(100);
				rand = BouncyUtil.encrypt(r, key.getPublic());
				CipherBlockAdd cipher = new CipherBlockAdd(r);
				data = secret.getBytes(MString.CHARSET_UTF_8);
				for (int i = 0; i < data.length; i++)
					data[i] = cipher.encode(data[i]);
			}
			this.pubKeyMd5 = MCrypt.md5(BouncyUtil.getPublicKey(key));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public CryptedString(String pubKey, String secret) {
		try {
			if (secret == null) {
				data = null;
				length = 0;
			} else {
				length = secret.length();
				byte[] r = BouncyUtil.createRand(100);
				rand = BouncyUtil.encrypt(r, BouncyUtil.getPublicKey(pubKey));
				CipherBlockAdd cipher = new CipherBlockAdd(r);
				data = secret.getBytes(MString.CHARSET_UTF_8);
				for (int i = 0; i < data.length; i++)
					data[i] = cipher.encode(data[i]);
			}
			this.pubKeyMd5 = MCrypt.md5(pubKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String value(KeyPair key) {
		if (data == null) return null;
		try {
			byte[] r = BouncyUtil.decrypt(rand, key.getPrivate());
			CipherBlockAdd cipher = new CipherBlockAdd(r);
			byte[] d = new byte[data.length];
			for (int i = 0; i < data.length; i++)
				d[i] = cipher.decode(data[i]);
			return new String(d, MString.CHARSET_UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String value(String privKey) {
		if (data == null) return null;
		try {
			PrivateKey key = BouncyUtil.getPrivateKey(privKey);
			
			byte[] r = BouncyUtil.decrypt(rand, key);
			CipherBlockAdd cipher = new CipherBlockAdd(r);
			byte[] d = new byte[data.length];
			for (int i = 0; i < data.length; i++)
				d[i] = cipher.decode(data[i]);
			return new String(d, MString.CHARSET_UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getPublicKeyMd5() {
		return pubKeyMd5;
	}
	
	@Override
	public String toString() {
		return "[***]";
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(length);
		if (data == null)
			out.writeInt(-1);
		else {
			out.writeInt(data.length);
			out.write(data);
			out.writeInt(rand.length);
			out.write(rand);
			out.writeObject(pubKeyMd5);
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		length = in.readInt();
		int len = in.readInt();
		if (len < 0) {
			data = null;
			rand = null;
		} else {
			data = new byte[len];
			MFile.readBinary(in, data, 0, len);
			int l = in.readInt();
			rand = new byte[l];
			MFile.readBinary(in, rand, 0, l);
			pubKeyMd5 = (String) in.readObject();
		}
	}
	
	public static KeyPair generateKey() {
		try {
			return BouncyUtil.generateKey();
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}
	
}
