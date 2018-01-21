package de.mhus.lib.core.util;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.crypt.MCrypt;

public final class SecureString {

	private byte[] data;
	private int length;
	
	public SecureString(String data) {
		this.length = data == null ? 0 : data.length();
		if (data == null) return;
		this.data = MCrypt.obfuscate(MString.toBytes(data));
	}
	
	public String value() {
		if (data == null) return null;
		return MString.toString(MCrypt.unobfuscate(data));
	}
	
	public int length() {
		return length;
	}

	public boolean isNull() {
		return data == null;
	}
	
}
