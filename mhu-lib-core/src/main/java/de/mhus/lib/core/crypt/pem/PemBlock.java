package de.mhus.lib.core.crypt.pem;

import de.mhus.lib.core.IReadProperties;

public interface PemBlock extends IReadProperties {
	
	String BLOCK_CIPHER = "CIPHER";
	String METHOD = "Method";
	String BLOCK_SIGN = "SIGNATURE";
	String BLOCK_PRIV = "PRIVATE KEY";
	String BLOCK_PUB  = "PUBLIC KEY";
	String LENGTH = "Length";
	String FORMAT = "Format";
	String IDENT = "Ident";
	String KEY_IDENT = "KeyIdent";
	String STRING_ENCODING = "Encoding";
	String PRIV_ID = "PrivKeyIdent";
	String PUB_ID = "PubKeyIdent";
	String DESCRIPTION = "Description";
	String CREATED = "Created";

	/**
	 * Returns the name of the block
	 * @return the name
	 */
	String getName();
	
	/**
	 * Returns the Block content without placeholders.
	 * 
	 * @return the text block
	 */
	String getBlock();

	byte[] getBytesBlock();
	
}
