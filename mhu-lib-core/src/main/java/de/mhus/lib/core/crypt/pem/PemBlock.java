/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	String ENCRYPTED = "Encrypted";
	String ENC_BLOWFISH = "blowfish";
	String BLOCK_HASH = "HASH";

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
