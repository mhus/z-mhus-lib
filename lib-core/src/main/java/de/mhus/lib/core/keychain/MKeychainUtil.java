/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.keychain;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MArgs;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.errors.NotSupportedException;

public class MKeychainUtil {

    public static MKeychain loadDefault() {
        MKeychain vault = M.l(MKeychain.class);
        checkDefault(vault);
        return vault;
    }

    public static void checkDefault(MKeychain vault) {
        KeychainSource def = vault.getSource(MKeychain.SOURCE_DEFAULT);
        if (def == null) {

            KeychainPassphrase vaultPassphrase = M.l(KeychainPassphrase.class);
            KeychainSourceFactory factory = M.l(KeychainSourceFactory.class);

            def = factory.create(MKeychain.SOURCE_DEFAULT, vaultPassphrase);
            if (def != null) vault.registerSource(def);
        }
    }

    public static void main(String[] in) throws IOException {
        MArgs args = new MArgs(in);

        MKeychain vault = loadDefault();

        KeychainSource source = null;
        if (args.contains("file")) {
            String vp = args.getValue("passphrase", "setit", 0);
            File f = new File(args.getValue("file", 0));
            source = new KeychainSourceFromSecFile(f, vp);
            vault.registerSource(source);
        }
        if (source == null) source = vault.getSource(MKeychain.SOURCE_DEFAULT);

        String cmd = args.getValue(MArgs.DEFAULT, "help", 0);

        switch (cmd) {
            case "help":
                {
                    System.out.println("Usage: <cmd> <args>");
                    System.out.println("list - list all keys");
                }
                break;
            case "list":
                {
                    ConsoleTable out = new ConsoleTable();
                    out.setHeaderValues("Source", "Id", "Type", "Description");
                    for (String sourceName : vault.getSourceNames()) {
                        source = vault.getSource(sourceName);
                        for (UUID id : source.getEntryIds()) {
                            KeyEntry entry = source.getEntry(id);
                            out.addRowValues(
                                    sourceName, id, entry.getType(), entry.getDescription());
                        }
                    }
                    out.print(System.out);
                }
                break;
        }
    }

    /**
     * Try to adapt the entry to the given class or interface.
     *
     * @param entry
     * @param ifc
     * @return The requested interface or class.
     * @throws NotSupportedException Thrown if the entry can't be adapted to the interface.
     * @throws ParseException
     */
    public static <T> T adaptTo(KeyEntry entry, Class<? extends T> ifc)
            throws ParseException, NotSupportedException {
        // delegate to service
        return M.l(KeyMutator.class).adaptTo(entry, ifc);
    }

    public static String getType(String content) {

        if (content == null) return MKeychain.TYPE_TEXT;

        // only analyse the first block in content
        int pos = content.indexOf("-----END ");
        if (pos < 0) return MKeychain.TYPE_TEXT;
        content = content.substring(0, pos);

        if (content.contains("-----BEGIN RSA PRIVATE KEY-----"))
            return MKeychain.TYPE_RSA_PRIVATE_KEY;
        if (content.contains("-----BEGIN RSA PUBLIC KEY-----"))
            return MKeychain.TYPE_RSA_PUBLIC_KEY;
        if (content.contains("-----BEGIN DSA PRIVATE KEY-----"))
            return MKeychain.TYPE_DSA_PRIVATE_KEY;
        if (content.contains("-----BEGIN DSA PUBLIC KEY-----"))
            return MKeychain.TYPE_DSA_PUBLIC_KEY;
        if (content.contains("-----BEGIN ECC PRIVATE KEY-----"))
            return MKeychain.TYPE_ECC_PRIVATE_KEY;
        if (content.contains("-----BEGIN ECC PUBLIC KEY-----"))
            return MKeychain.TYPE_ECC_PUBLIC_KEY;
        if (content.contains("-----BEGIN PRIVATE KEY-----")) {
            if (content.contains("Method: AES")) return MKeychain.TYPE_AES_PRIVATE_KEY;
            if (content.contains("Method: RSA")) return MKeychain.TYPE_RSA_PRIVATE_KEY;
            if (content.contains("Method: ECC")) return MKeychain.TYPE_ECC_PRIVATE_KEY;
            if (content.contains("Method: DSA")) return MKeychain.TYPE_DSA_PRIVATE_KEY;
        }
        if (content.contains("-----BEGIN PUBLIC KEY-----")) {
            if (content.contains("Method: AES")) return MKeychain.TYPE_AES_PUBLIC_KEY;
            if (content.contains("Method: RSA")) return MKeychain.TYPE_RSA_PUBLIC_KEY;
            if (content.contains("Method: ECC")) return MKeychain.TYPE_ECC_PUBLIC_KEY;
            if (content.contains("Method: DSA")) return MKeychain.TYPE_DSA_PUBLIC_KEY;
        }
        if (content.contains("-----BEGIN CIPHER-----")) return MKeychain.TYPE_CIPHER;
        if (content.contains("-----BEGIN SIGNATURE-----")) return MKeychain.TYPE_SIGNATURE;
        else return MKeychain.TYPE_TEXT;
    }

    /**
     * Try to adapt the source to the given class or interface.
     *
     * @param source
     * @param ifc
     * @return The requested interface or class.
     * @throws NotSupportedException Thrown if the source can't be adapted to the interface.
     */
    //	@SuppressWarnings("unchecked")
    //	public static <T> T adaptTo(VaultSource source, Class<? extends T> ifc) throws
    // NotSupportedException {
    //		if (ifc.isInstance(source)) return (T) source;
    //		throw new NotSupportedException(source,ifc);
    //	}

}
