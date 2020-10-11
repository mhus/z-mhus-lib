/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.crypt.pem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.errors.MRuntimeException;

public class PemBlockModel extends MProperties implements PemBlock {

    private String name;
    protected String block;
    private String rest;

    private static final int BLOCK_WIDTH = 50;

    public PemBlockModel() {}

    public PemBlockModel(String name) {
        setName(name);
    }

    public PemBlockModel(String name, String block) {
        setName(name);
        setBlock(block);
    }

    public PemBlockModel(String name, byte[] block) {
        setName(name);
        setBlock(block);
    }

    public PemBlockModel(PemBlock clone) {
        setName(clone.getName());
        this.block = clone.getBlock();
        for (java.util.Map.Entry<String, Object> item : clone.entrySet())
            put(item.getKey(), item.getValue());
    }

    public PemBlockModel parse(String block) throws ParseException {
        // parse

        // find start
        int p = block.indexOf("-----BEGIN ");
        if (p < 0) throw new ParseException("start of block not found");

        block = block.substring(p + 11);
        // get name
        p = block.indexOf("-----");
        if (p < 0) throw new ParseException("end of header not found");
        String n = block.substring(0, p);
        if (n.contains("\n") || n.contains("\r"))
            throw new ParseException("name contains line break", n);
        setName(n);
        block = block.substring(p + 5);

        // find end
        String endMark = "-----END " + getName() + "-----";
        p = block.indexOf(endMark);
        if (p < 0) throw new ParseException("end of block not found", getName());

        rest = block.substring(p + endMark.length()).trim();
        block = block.substring(0, p).trim(); // remove line break

        // read lines
        boolean params = true;
        String blockOrg = "";
        String lastKey = null;
        while (true) {
            String line = block;
            p = block.indexOf('\n');
            if (p >= 0) {
                line = block.substring(0, p);
                block = block.substring(p + 1);
            }
            if (params) {
                String l = line.trim();
                if (l.length() == 0) {
                    params = false;
                } else if (line.startsWith(" ") && lastKey != null) {
                    setString(lastKey, getString(lastKey, "") + line.substring(1));
                } else {
                    int pp = line.indexOf(':');
                    if (pp < 0) {
                        //	throw new ParseException("Parameter key not identified",line);
                        // start of the block
                        params = false;
                        blockOrg = line;
                    } else {
                        lastKey = line.substring(0, pp).trim();
                        String value = line.substring(pp + 1).trim();
                        setString(lastKey, value);
                    }
                }
            } else {
                //				if (line.length() == 0)
                //					break; // end of block
                blockOrg = blockOrg + line;
            }

            if (p < 0) break; // end of block
        }

        // decode unicode
        this.block = MString.decodeUnicode(blockOrg);

        return this;
    }

    public String getRest() {
        return rest;
    }

    protected void setName(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBlock() {
        return block;
    }

    @SuppressWarnings("unchecked")
    public <T extends PemBlockModel> T setBlock(String block) {
        this.block = block;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends PemBlockModel> T set(String key, Object value) {
        put(key, value);
        return (T) this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(getName()).append("-----\n");
        for (java.util.Map.Entry<String, Object> item : entrySet()) {
            String key = item.getKey().trim();
            sb.append(key).append(": ");
            int len = key.length() + 2;
            String value = String.valueOf(item.getValue());
            if (len + value.length() <= BLOCK_WIDTH) sb.append(item.getValue()).append('\n');
            else {
                sb.append(value.substring(0, BLOCK_WIDTH - len)).append('\n');
                len = BLOCK_WIDTH - len;
                while (len < value.length()) {
                    sb.append(' ');
                    if (len + BLOCK_WIDTH - 1 > value.length()) {
                        sb.append(value.substring(len)).append('\n');
                        break;
                    } else {
                        sb.append(value.substring(len, len + BLOCK_WIDTH - 1)).append('\n');
                    }
                    len = len + BLOCK_WIDTH - 1;
                }
            }
        }
        sb.append('\n');

        sb.append(getEncodedBlock());
        sb.append("\n\n");
        sb.append("-----END ").append(getName()).append("-----\n");
        return sb.toString();
    }

    public String getEncodedBlock() {
        // encode all unusual characters
        String b = MString.encodeUnicode(block, true);
        // transform to block
        StringBuilder c = new StringBuilder();
        while (b.length() > BLOCK_WIDTH) {
            c.append(b.substring(0, BLOCK_WIDTH)).append('\n');
            b = b.substring(BLOCK_WIDTH);
        }
        c.append(b); // the rest of b
        return c.toString();
    }

    @SuppressWarnings("unchecked")
    public <T extends PemBlockModel> T setBlock(byte[] bytes) {
        //		setBlock(Base64.encode(bytes));
        setBlock(Base64.getEncoder().encodeToString(bytes));
        return (T) this;
    }

    @Override
    public byte[] getBytesBlock() {
        //		return Base64.decode(getBlock());
        return Base64.getDecoder().decode(block);
    }
    
    @Override
    public boolean save(File file) throws IOException {
        return MFile.writeFile(file, toString());
    }

    @Override
    public boolean save(File file, boolean addDate) throws IOException {
        return MFile.writeFile(file, toString());
    }

    @Override
    public String saveToString() {
        return toString();
    }

    @Override
    public String saveToString(boolean addDate) {
        return toString();
    }

    @Override
    public boolean save(OutputStream out, boolean addDate) throws IOException {
        return MFile.writeFile(out, toString());
    }

    @Override
    public boolean save(OutputStream out) throws IOException {
        return MFile.writeFile(out, toString());
    }

    public static PemBlockModel load(File f) {
        try {
            return new PemBlockModel().parse(MFile.readFile(f));
        } catch (ParseException e) {
            throw new MRuntimeException(f,e);
        }
    }

    public static PemBlockModel load(InputStream inStream) throws IOException {
        try {
            return new PemBlockModel().parse(MFile.readFile(inStream));
        } catch (ParseException e) {
            throw new MRuntimeException(e);
        }
    }

    public static PemBlockModel loadFromString(String content) {
        try {
            return new PemBlockModel().parse(content);
        } catch (ParseException e) {
            throw new MRuntimeException(e);
        }
    }

}
