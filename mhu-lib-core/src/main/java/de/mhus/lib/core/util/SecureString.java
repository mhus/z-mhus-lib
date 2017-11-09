package de.mhus.lib.core.util;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.crypt.MCrypt;

public final class SecureString {

	private byte[] data;
	
	public SecureString(String data) {
		this.data = MCrypt.obfuscate(MString.toBytes(data));
	}
	
	public String value() {
		return MString.toString(MCrypt.unobfuscate(data));
	}
	
}
