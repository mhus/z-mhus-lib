/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.vault;

import java.util.UUID;

import de.mhus.lib.core.crypt.pem.PemBlock;
import de.mhus.lib.core.crypt.pem.PemUtil;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.util.SecureString;

public class PemEntry extends DefaultEntry {

    public PemEntry(String entry) throws ParseException {
        this(PemUtil.parse(entry));
    }

    public PemEntry(PemBlock block) {
        {
            String str = block.getString(PemBlock.IDENT, null);
            if (str == null) id = UUID.randomUUID();
            else id = UUID.fromString(str);
        }
        {
            description = block.getString(PemBlock.DESCRIPTION, "");
        }
        {
            String method = block.getString(PemBlock.METHOD, "").toUpperCase();
            if (PemUtil.isPubKey(block)) {
                if (method.contains("RSA")) type = MVault.TYPE_RSA_PUBLIC_KEY;
                else if (method.contains("DSA")) type = MVault.TYPE_DSA_PUBLIC_KEY;
                else if (method.contains("ECC")) type = MVault.TYPE_ECC_PUBLIC_KEY;
                else type = "?" + MVault.SUFFIX_CIPHER_PUBLIC_KEY;
            } else if (PemUtil.isPrivKey(block)) {
                if (method.contains("RSA")) type = MVault.TYPE_RSA_PRIVATE_KEY;
                else if (method.contains("DSA")) type = MVault.TYPE_DSA_PRIVATE_KEY;
                else if (method.contains("ECC")) type = MVault.TYPE_ECC_PRIVATE_KEY;
                else type = "?" + MVault.SUFFIX_CIPHER_PRIVATE_KEY;
            } else type = MVault.TYPE_TEXT;
        }
        value = new SecureString(block.toString());
    }
}
