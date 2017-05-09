package de.mhus.lib.core.crypt;

public interface CipherBlock {

	void reset();
	byte encode(byte in);
	byte decode(byte in);
	
}
