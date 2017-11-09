package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.InputStream;

public class CipherInputStream extends InputStream {

	private InputStream is;
	private CipherBlock cipher;
	
	public CipherInputStream(InputStream is) {
		this.is = is;
	}
	
	public CipherInputStream(InputStream is, CipherBlock cipher) {
		this.is = is;
		this.cipher = cipher;
	}
	
	@Override
	public int read() throws IOException {
		int out = is.read();
		if (out < 0 || cipher == null) return out;
		return cipher.decode((byte)out);
	}

	public CipherBlock getCipher() {
		return cipher;
	}

	public void setCipher(CipherBlock cipher) {
		this.cipher = cipher;
	}

}
