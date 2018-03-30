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

import java.io.IOException;
import java.util.UUID;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MValidator;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.vault.MVault;
import de.mhus.lib.core.vault.MVaultUtil;
import de.mhus.lib.core.vault.VaultEntry;
import de.mhus.lib.errors.NotSupportedException;

public class PemUtil {

	public static PemPriv signPrivFromString(String str) throws Exception, NotSupportedException, IOException {
		
		if (MValidator.isUUID(str)) {
			MVault vault = MVaultUtil.loadDefault();
			VaultEntry entry = vault.getEntry(UUID.fromString(str));
			PemPriv key = entry.adaptTo(PemPriv.class);
			return key;
		}

		if (isPemBlock(str)) {
			PemBlockModel block = new PemBlockModel().parse(str);
//			return new PemKey(PemBlock.BLOCK_CIPHER).set(PemBlock.METHOD, block.getString(PemBlock.METHOD,"")).setBlock(block.getEncodedBlock());
			return new PemKey(block);
		}

		String name = MString.beforeIndex(str, ':');
		String key = MString.afterIndex(str, ':');
		return new PemKey(PemBlock.BLOCK_SIGN).set(PemBlock.METHOD, name).setBlock(key);
	}
	
	public static PemPub signPubFromString(String str) throws NotSupportedException, IOException, ParseException {
		
		if (MValidator.isUUID(str)) {
			MVault vault = MVaultUtil.loadDefault();
			VaultEntry entry = vault.getEntry(UUID.fromString(str));
			PemPub key = entry.adaptTo(PemPub.class);
			return key;
		}

		if (isPemBlock(str)) {
			PemBlockModel block = new PemBlockModel().parse(str);
//			return new PemKey(PemBlock.BLOCK_CIPHER).set(PemBlock.METHOD, block.getString(PemBlock.METHOD,"")).setBlock(block.getEncodedBlock());
			return new PemKey(block);
		}

		String name = MString.beforeIndex(str, ':');
		String key = MString.afterIndex(str, ':');
		return new PemKey(PemBlock.BLOCK_SIGN).set(PemBlock.METHOD, name).setBlock(key);
	}

	public static PemPriv cipherPrivFromString(String str) throws ParseException, NotSupportedException, IOException {
		
		if (MValidator.isUUID(str)) {
			MVault vault = MVaultUtil.loadDefault();
			VaultEntry entry = vault.getEntry(UUID.fromString(str));
			PemPriv key = entry.adaptTo(PemPriv.class);
			return key;
		}

		if (isPemBlock(str)) {
			PemBlockModel block = new PemBlockModel().parse(str);
//			return new PemKey(PemBlock.BLOCK_CIPHER).set(PemBlock.METHOD, block.getString(PemBlock.METHOD,"")).setBlock(block.getEncodedBlock());
			return new PemKey(block);
		}

		String name = MString.beforeIndex(str, ':').toUpperCase().trim();
		String key = MString.afterIndex(str, ':').trim();
		return new PemKey(PemBlock.BLOCK_CIPHER).set(PemBlock.METHOD, name).setBlock(key);
	}
	
	public static boolean isPemBlock(String text) {
		if (text == null) return false;
		
		int p1 = text.indexOf("-----START");
		int p2 = text.indexOf("-----END");
		return text.indexOf("\n") >= 0 && p1 >= 0 && p2 > 0 && p2 > p1;

	}
	
	public static PemPub cipherPubFromString(String str) throws ParseException, NotSupportedException, IOException {
		
		if (MValidator.isUUID(str)) {
			MVault vault = MVaultUtil.loadDefault();
			VaultEntry entry = vault.getEntry(UUID.fromString(str));
			PemPub key = entry.adaptTo(PemPub.class);
			return key;
		}

		if (isPemBlock(str)) {
			PemBlockModel block = new PemBlockModel().parse(str);
//			return new PemKey(PemBlock.BLOCK_CIPHER).set(PemBlock.METHOD, block.getString(PemBlock.METHOD,"")).setBlock(block.getEncodedBlock());
			return new PemKey(block);
		}
		
		String name = MString.beforeIndex(str, ':');
		String key = MString.afterIndex(str, ':');
		return new PemKey(PemBlock.BLOCK_CIPHER).set(PemBlock.METHOD, name).setBlock(key);
	}

	public static String toLine(PemBlock key) {
		return key.getString(PemBlock.METHOD, "?") + ":" + key.getBlock();
	}
	
}
