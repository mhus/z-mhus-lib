package de.mhus.lib.core.vault;

import java.util.UUID;

import de.mhus.lib.core.crypt.pem.PemBlock;
import de.mhus.lib.core.crypt.pem.PemUtil;
import de.mhus.lib.core.parser.ParseException;

public class PemEntry extends DefaultEntry {

	public PemEntry(String entry) throws ParseException {
		this(PemUtil.parse(entry));
	}
	
	public PemEntry(PemBlock block)  {
		{
			String str = block.getString(PemBlock.IDENT, null);
			if (str == null)
				id = UUID.randomUUID();
			else
				id = UUID.fromString(str);
		}
		{
			description = block.getString(PemBlock.DESCRIPTION, "");
		}
		{
			String method = block.getString(PemBlock.METHOD, "").toUpperCase();
			if (PemUtil.isPubKey(block)) {
				if (method.contains("RSA"))
					type = MVault.TYPE_RSA_PUBLIC_KEY;
				else
				if (method.contains("DSA"))
					type = MVault.TYPE_DSA_PUBLIC_KEY;
				else
				if (method.contains("ECC"))
					type = MVault.TYPE_ECC_PUBLIC_KEY;
				else
					type = "?" + MVault.SUFFIX_CIPHER_PUBLIC_KEY;
			} else
			if (PemUtil.isPrivKey(block)) {
				if (method.contains("RSA"))
					type = MVault.TYPE_RSA_PRIVATE_KEY;
				else
				if (method.contains("DSA"))
					type = MVault.TYPE_DSA_PRIVATE_KEY;
				else
				if (method.contains("ECC"))
					type = MVault.TYPE_ECC_PRIVATE_KEY;
				else
					type = "?" + MVault.SUFFIX_CIPHER_PRIVATE_KEY;
			} else
				type = MVault.TYPE_TEXT;
		}
	}
	
}
