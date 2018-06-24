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
package de.mhus.lib.core;

import java.io.BufferedWriter;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.util.MUri;
import de.mhus.lib.core.util.SetCast;

public class MProperties extends AbstractProperties implements Externalizable {

	private static final long serialVersionUID = 1L;
	
	protected Properties properties = null;
	private static final char[] hexDigit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	
	public MProperties() {
		this(new Properties());
	}
	
	public MProperties(String ... values) {
		this(new Properties());
		if (values != null) {
			for (int i = 0; i < values.length; i+=2) {
				if (i+1 < values.length)
					setString(values[i], values[i+1]);
			}
		}
	}
	
	public MProperties(Dictionary<?, ?> config) {
		this.properties = new Properties();
		for (Enumeration<?> enu = config.keys(); enu.hasMoreElements();) {
			Object next = enu.nextElement();
			this.properties.put(String.valueOf( next ), config.get(next));
		}
	}
	
	public MProperties(Map<?, ?> in) {
		this.properties = new Properties();
		for (Map.Entry<?, ?> e : in.entrySet())
			if (e.getKey() != null && e.getValue() != null)
				this.properties.put(String.valueOf( e.getKey() ), e.getValue());
	}
	
	public MProperties(IReadProperties in) {
		this.properties = new Properties();
		for (Map.Entry<?, ?> e : in.entrySet())
			if (e.getKey() != null && e.getValue() != null)
				this.properties.put(String.valueOf( e.getKey() ), e.getValue());
	}
	
	// need this constructor to avoid ambiguous references for IProperties 
	public MProperties(IProperties in) {
		this.properties = new Properties();
		for (Map.Entry<?, ?> e : in.entrySet())
			if (e.getKey() != null && e.getValue() != null)
				this.properties.put(String.valueOf( e.getKey() ), e.getValue());
	}
	
	public MProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		if (value == null)
			properties.remove(key);
		else
			properties.put(key, value );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Set<String> keys() {
		return new SetCast<Object, String>(properties.keySet());
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, properties);
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject( properties);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		properties = (Properties) in.readObject();
	}

	/**
	 * This will handle the strings like options. Means a string without separator will handled
	 * as key and set to true. e.g. val1&val2&a=b will be val1=true, val2=true, a=b
	 * 
	 * @param properties Rfc1738 (Url Encode) encoded string
	 * @return The MProperties
	 */
	public static MProperties explodeToOptions(String properties) {
		return explodeToMProperties(MUri.explodeArray(properties), '=');
	}

	/**
	 * This will handle the strings like options. Means a string without separator will handled
	 * as key and set to true. e.g. [val1, val2, a=b] will be val1=true, val2=true, a=b
	 * 
	 * @param properties
	 * @return The MProperties
	 */
	public static MProperties explodeToOptions(String[] properties) {
		return explodeToOptions(properties, '=');
	}

	/**
	 * This will handle the strings like properties. Means a string without separator will be
	 * stored as value with an increasing key as integer, e.g. val1&val2&a=b will be 0=val1, 1=val2, a=b
	 * @param properties Rfc1738 (Url Encoded) encoded string
	 * @return The MProperties
	 */
	public static MProperties explodeToMProperties(String properties) {
		return explodeToMProperties(MUri.explodeArray(properties), '=');
	}

	/**
	 * This will handle the strings like properties. Means a string without separator will be
	 * stored as value with an increasing key as integer, e.g. [val1, val2, a=b] will be 0=val1, 1=val2, a=b
	 * @param properties
	 * @return The MProperties
	 */
	public static MProperties explodeToMProperties(String[] properties) {
		return explodeToMProperties(properties, '=');
	}
	
	/**
	 * This will handle the strings like options. Means a string without separator will handled
	 * as key and set to true. e.g. [val1, val2, a=b] will be val1=true, val2=true, a=b
	 * 
	 * @param properties
	 * @param separator
	 * @return The MProperties
	 */
	public static MProperties explodeToOptions(String[] properties, char separator) {
		MProperties p = new MProperties();
		if (properties != null) {
			for (String i : properties) {
				if (i != null) {
					int idx = i.indexOf(separator);
					if (idx >= 0) {
						p.setProperty(i.substring(0,idx).trim(),i.substring(idx+1));
					} else {
						p.setProperty(i, true);
					}
				}
			}
		}
		return p;
	}

	/**
	 * This will handle the strings like properties. Means a string without separator will be
	 * stored as value with an increasing key as integer, e.g. [val1, val2, a=b] will be 0=val1, 1=val2, a=b
	 * @param properties
	 * @param separator
	 * @return The MProperties
	 */
	public static MProperties explodeToMProperties(String[] properties, char separator) {
		MProperties p = new MProperties();
		if (properties != null) {
			int cnt = 0;
			for (String i : properties) {
				if (i != null) {
					int idx = i.indexOf(separator);
					if (idx >= 0) {
						p.setProperty(i.substring(0,idx).trim(),i.substring(idx+1));
					} else {
						p.setProperty(String.valueOf(cnt), i);
						cnt++;
					}
				}
			}
		}
		return p;
	}
	
	/**
	 * This will handle the strings like properties. Means a string without separator will be
	 * stored as value with an increasing key as integer, e.g. [val1, val2, a=b] will be 0=val1, 1=val2, a=b
	 * @param properties
	 * @return The Properties
	 */
	public static Properties explodeToProperties(String[] properties) {
		Properties p = new Properties();
		if (properties != null) {
			for (String i : properties) {
				if (i != null) {
					int idx = i.indexOf('=');
					if (idx >= 0) {
						p.setProperty(i.substring(0,idx).trim(),i.substring(idx+1));
					}
				}
			}
		}
		return p;
	}

	@Override
	public boolean containsValue(Object value) {
		return properties.containsValue(value);
	}

	@Override
	public Collection<Object> values() {
		return properties.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		HashMap<String, Object> wrapper = new HashMap<>();
		for (java.util.Map.Entry<Object, Object> entry : properties.entrySet())
			wrapper.put( String.valueOf( entry.getKey() ), entry.getValue() );
		return wrapper.entrySet();
	}

	public static MProperties load(String fileName) {
		Properties p = new Properties();
		try {
			File f = new File(fileName);
			if (f.exists() && f.isFile()) {
				FileInputStream is = new FileInputStream(f);
				p.load(is);
			}
		} catch (Throwable t) {
			MLogUtil.log().d(fileName, t);
		}
		MProperties out = new MProperties(p);
		return out;
	}

	public static MProperties loadOrEmpty(File f) {
		MProperties out = load(f);
		if (out == null) out = new MProperties();
		return out;
	}
	
	public static MProperties load(File f) {
		try {
			FileInputStream fis = new FileInputStream(f);
			MProperties out = load(fis);
			fis.close();
			return out;
		} catch (IOException e) {
			return null;
		}
//		Properties p = new Properties();
//		try {
//			if (f.exists() && f.isFile()) {
//				FileInputStream is = new FileInputStream(f);
//				p.load(is);
//			}
//		} catch (Throwable t) {
//			MLogUtil.log().d(f, t);
//		}
//		MProperties out = new MProperties(p);
//		return out;
	}
	
    public static MProperties load(InputStream inStream) throws IOException {
    	MProperties out = new MProperties();
        out.load0(new LineReader(inStream));
        return out;
    }

    private void load0 (LineReader lr) throws IOException {
        char[] convtBuf = new char[1024];
        int limit;
        int keyLen;
        int valueStart;
        char c;
        boolean hasSep;
        boolean precedingBackslash;

        while ((limit = lr.readLine()) >= 0) {
            c = 0;
            keyLen = 0;
            valueStart = limit;
            hasSep = false;

            //System.out.println("line=<" + new String(lineBuf, 0, limit) + ">");
            precedingBackslash = false;
            while (keyLen < limit) {
                c = lr.lineBuf[keyLen];
                //need check if escaped.
//                if ((c == '=' ||  c == ':') && !precedingBackslash) {
                if (c == '=' && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    hasSep = true;
                    break;
                } else if ((c == ' ' || c == '\t' ||  c == '\f') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    break;
                }
                if (c == '\\') {
                    precedingBackslash = !precedingBackslash;
                } else {
                    precedingBackslash = false;
                }
                keyLen++;
            }
            while (valueStart < limit) {
                c = lr.lineBuf[valueStart];
                if (c != ' ' && c != '\t' &&  c != '\f') {
//                    if (!hasSep && (c == '=' ||  c == ':')) {
                    if (!hasSep && c == '=') {
                        hasSep = true;
                    } else {
                        break;
                    }
                }
                valueStart++;
            }
            String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
            String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
            put(key, value);
        }
    }
	
    private static class LineReader {
        public LineReader(InputStream inStream) {
            this.inStream = inStream;
            inByteBuf = new byte[8192];
        }

//        public LineReader(Reader reader) {
//            this.reader = reader;
//            inCharBuf = new char[8192];
//        }

        byte[] inByteBuf;
        char[] inCharBuf;
        char[] lineBuf = new char[1024];
        int inLimit = 0;
        int inOff = 0;
        InputStream inStream;
        Reader reader;

        int readLine() throws IOException {
            int len = 0;
            char c = 0;

            boolean skipWhiteSpace = true;
            boolean isCommentLine = false;
            boolean isNewLine = true;
            boolean appendedLineBegin = false;
            boolean precedingBackslash = false;
            boolean skipLF = false;

            while (true) {
                if (inOff >= inLimit) {
                    inLimit = (inStream==null)?reader.read(inCharBuf)
                                              :inStream.read(inByteBuf);
                    inOff = 0;
                    if (inLimit <= 0) {
                        if (len == 0 || isCommentLine) {
                            return -1;
                        }
                        if (precedingBackslash) {
                            len--;
                        }
                        return len;
                    }
                }
                if (inStream != null) {
                    //The line below is equivalent to calling a
                    //ISO8859-1 decoder.
                    c = (char) (0xff & inByteBuf[inOff++]);
                } else {
                    c = inCharBuf[inOff++];
                }
                if (skipLF) {
                    skipLF = false;
                    if (c == '\n') {
                        continue;
                    }
                }
                if (skipWhiteSpace) {
                    if (c == ' ' || c == '\t' || c == '\f') {
                        continue;
                    }
                    if (!appendedLineBegin && (c == '\r' || c == '\n')) {
                        continue;
                    }
                    skipWhiteSpace = false;
                    appendedLineBegin = false;
                }
                if (isNewLine) {
                    isNewLine = false;
                    if (c == '#' || c == '!') {
                        isCommentLine = true;
                        continue;
                    }
                }

                if (c != '\n' && c != '\r') {
                    lineBuf[len++] = c;
                    if (len == lineBuf.length) {
                        int newLength = lineBuf.length * 2;
                        if (newLength < 0) {
                            newLength = Integer.MAX_VALUE;
                        }
                        char[] buf = new char[newLength];
                        System.arraycopy(lineBuf, 0, buf, 0, lineBuf.length);
                        lineBuf = buf;
                    }
                    //flip the preceding backslash flag
                    if (c == '\\') {
                        precedingBackslash = !precedingBackslash;
                    } else {
                        precedingBackslash = false;
                    }
                }
                else {
                    // reached EOL
                    if (isCommentLine || len == 0) {
                        isCommentLine = false;
                        isNewLine = true;
                        skipWhiteSpace = true;
                        len = 0;
                        continue;
                    }
                    if (inOff >= inLimit) {
                        inLimit = (inStream==null)
                                  ?reader.read(inCharBuf)
                                  :inStream.read(inByteBuf);
                        inOff = 0;
                        if (inLimit <= 0) {
                            if (precedingBackslash) {
                                len--;
                            }
                            return len;
                        }
                    }
                    if (precedingBackslash) {
                        len -= 1;
                        //skip the leading whitespace characters in following line
                        skipWhiteSpace = true;
                        appendedLineBegin = true;
                        precedingBackslash = false;
                        if (c == '\r') {
                            skipLF = true;
                        }
                    } else {
                        return len;
                    }
                }
            }
        }
    }

    private String loadConvert (char[] in, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if(aChar == 'u') {
                    // Read the xxxx
                    int value=0;
                    for (int i=0; i<4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                          case '0': case '1': case '2': case '3': case '4':
                          case '5': case '6': case '7': case '8': case '9':
                             value = (value << 4) + aChar - '0';
                             break;
                          case 'a': case 'b': case 'c':
                          case 'd': case 'e': case 'f':
                             value = (value << 4) + 10 + aChar - 'a';
                             break;
                          case 'A': case 'B': case 'C':
                          case 'D': case 'E': case 'F':
                             value = (value << 4) + 10 + aChar - 'A';
                             break;
                          default:
                              throw new IllegalArgumentException(
                                           "Malformed \\uxxxx encoding.");
                        }
                     }
                    out[outLen++] = (char)value;
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String (out, 0, outLen);
    }

//	public static MProperties load(InputStream is) {
//		Properties p = new Properties();
//		try {
//			p.load(is);
//		} catch (Throwable t) {
//			MLogUtil.log().d(t);
//		}
//		MProperties out = new MProperties(p);
//		return out;
//	}
	
	public static MProperties load(Reader is) {
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (Throwable t) {
			MLogUtil.log().d(t);
		}
		MProperties out = new MProperties(p);
		return out;
	}
	
	@Override
	public int size() {
		return properties.size();
	}
	
	public boolean save(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		boolean ret = save(fos);
		fos.close();
		return ret;
	}

	public boolean save(OutputStream out) throws IOException {
		store(new BufferedWriter(new OutputStreamWriter(out, MString.CHARSET_UTF_8)),true);
		return true;
    }

   private void store(BufferedWriter bw, boolean escUnicode)
            throws IOException
        {
            bw.write("#" + new Date().toString());
            bw.newLine();
            synchronized (this) {
            	for (String key : keys()) {
                    String val = (String)getString(key, "");
                    key = saveConvert(key, true, escUnicode);
                    /* No need to escape embedded and trailing spaces for value, hence
                     * pass false to flag.
                     */
                    val = saveConvert(val, false, escUnicode);
                    bw.write(key + "=" + val);
                    bw.newLine();
                }
            }
            bw.flush();
        }

   private String saveConvert(String value, boolean escapeSpace, boolean escapeUnicode) {
		int len = value.length();
		int bufLen = len * 2;
		if (bufLen < 0) bufLen = Integer.MAX_VALUE;
		StringBuilder outBuffer = new StringBuilder(bufLen);
		for(int x=0; x<len; x++) {
			char aChar = value.charAt(x);
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\'); outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch(aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\t':outBuffer.append('\\'); outBuffer.append('t');
			break;
			case '\n':outBuffer.append('\\'); outBuffer.append('n');
			break;
			case '\r':outBuffer.append('\\'); outBuffer.append('r');
			break;
			case '\f':outBuffer.append('\\'); outBuffer.append('f');
			break;
			case '=': 
			case ':': 
			case '#': 
			case '!':
				outBuffer.append('\\'); outBuffer.append(aChar);
				break;
			default:
				if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode ) {
				    outBuffer.append('\\');
				    outBuffer.append('u');
				    outBuffer.append(toHex((aChar >> 12) & 0xF));
				    outBuffer.append(toHex((aChar >>  8) & 0xF));
				    outBuffer.append(toHex((aChar >>  4) & 0xF));
				    outBuffer.append(toHex( aChar        & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}
   
   private static char toHex(int nibble) {
       return hexDigit[(nibble & 0xF)];
   }

	@Override
	public void clear() {
		properties.clear();
	}
   	
	/**
	 * In this scenario we separate between functional parameters (starting with one underscore)
	 * and data. Using this method functional parameters can be cascaded over multiple
	 * levels.
	 * 
	 * Will remove all parameters starting with underscore and not two underscore and
	 * remove one underscore from thoos with more underscores.
	 * 
	 * _test will be removed
	 * __test will be _test
	 * 
	 * after update
	 * 
	 * @param in
	 */
	public static void updateFunctional(Map<String,Object> in) {
		in.keySet().removeIf(k -> isFunctional(k));
		for (String key : new LinkedList<>(in.keySet()))
			if (key.startsWith("_"))
				in.put(key.substring(1), in.remove(key));
	}
	
	/**
	 * Return true if key starts with underscore but not with two underscores.
	 * 
	 * @param key
	 * @return true if actual internal
	 */
	public static boolean isFunctional(String key) {
		return key.startsWith("_") && !key.startsWith("__");
 	}
	
	
}
