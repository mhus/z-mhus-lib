package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.OutputStream;

public class CipherOutputStream extends OutputStream {

	private CipherBlock cipher;
	private OutputStream os;
	
	public CipherOutputStream(OutputStream os) {
		this.os = os;
	}
	
	@Override
	public void write(int b) throws IOException {
		if (cipher == null) 
			os.write(b);
		else
			os.write(cipher.encode((byte)(b-128))+128);
	}

	public CipherBlock getCipher() {
		return cipher;
	}

	public void setCipher(CipherBlock cipher) {
		this.cipher = cipher;
	}

}
