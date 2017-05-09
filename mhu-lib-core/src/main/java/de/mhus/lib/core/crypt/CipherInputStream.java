package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.InputStream;

public class CipherInputStream extends InputStream {

	private InputStream is;
	private CipherBlock cipher;
	
	public CipherInputStream(InputStream is) {
		this.is = is;
	}
	
	@Override
	public int read() throws IOException {
		int out = is.read();
		if (out < 0 || cipher == null) return out;
		return cipher.decode((byte)(out-128))+128;
	}

	public CipherBlock getCipher() {
		return cipher;
	}

	public void setCipher(CipherBlock cipher) {
		this.cipher = cipher;
	}

}
