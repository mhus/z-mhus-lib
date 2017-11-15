package de.mhus.lib.core.crypt.pem;

public interface PemPair {

	PemPub getPublic();
	
	PemPriv getPrivate();
	
}
