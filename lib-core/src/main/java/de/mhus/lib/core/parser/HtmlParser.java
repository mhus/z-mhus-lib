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
package de.mhus.lib.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Map;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;

/** @author hummel */
public class HtmlParser {

    public static final int TEXT = 1;
    public static final int TAG = 2;

    private Reader is = null;
    private Listener listener = null;
    private boolean trim = false;

    public HtmlParser() {}

    /**
     * Creates a new instance of Parser
     *
     * @param _is
     * @param _listener
     */
    public HtmlParser(InputStream _is, Listener _listener) {
        is = new InputStreamReader(_is);
        listener = _listener;
    }

    public HtmlParser(Reader _is, Listener _listener) {
        is = _is;
        listener = _listener;
    }

    public boolean parse(Reader _is, Listener _listener) {
        is = _is;
        listener = _listener;
        return parse();
    }

    public boolean parse() {

        int type = TEXT;
        StringBuilder text = new StringBuilder();
        char[] one = new char[1];

        try {
            while (true) {

                int i = is.read(one);
                if (i != 1) break;

                char c = one[0];
                // System.out.println( "Char: " + c );

                switch (type) {
                    case TEXT:
                        if (c == '<') {
                            type = TAG;
                            trim(text);
                            if (text.length() > 0
                                    && !listener.foundText(MXml.decode(text.toString())))
                                return false;
                            text = new StringBuilder();
                        } else {
                            text.append(c);
                        }
                        break;
                    case TAG:
                        if (c == '>') {
                            type = TEXT;
                            String tag = text.toString().trim();
                            // System.out.println( "Tag: [" + tag + "] Length: " +
                            // tag.length() );
                            if (tag.length() > 0) {
                                if (tag.startsWith("!--") && tag.endsWith("--")) {
                                    // System.out.println( "Send Note: " +
                                    // tag.substring( 3, tag.length()-2 ) );
                                    if (!listener.foundNote(tag.substring(3, tag.length() - 2)))
                                        return false;
                                } else if (tag.startsWith("?") && tag.endsWith("?")) {
                                    if (!listener.foundProcessorInstruction(
                                            tag.substring(1, tag.length() - 1).trim()))
                                        return false;
                                } else if (tag.startsWith("/")) {
                                    if (!listener.foundTagClose(tag.substring(1).trim()))
                                        return false;
                                } else if (tag.endsWith("/")) {
                                    String tagx = tag.substring(0, tag.length() - 1).trim();
                                    if (!listener.foundSingleTag(
                                            getTagName(tagx), getTagParams(tagx))) return false;
                                } else {
                                    if (!listener.foundTagOpen(getTagName(tag), getTagParams(tag)))
                                        return false;
                                }
                            } else {
                                System.err.println("Tag without name");
                                return true;
                            }
                            text = new StringBuilder();
                        } else {
                            text.append(c);
                        }
                        break;
                }
            }

            if (type == TEXT) {
                trim(text);
                if (text.length() > 0 && !listener.foundText(MXml.decode(text.toString())))
                    return false;
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return true;
        }

        return true;
    }

    protected void trim(StringBuilder text) {
        if (!trim) return;
        while (text.length() > 0 && MString.isWhitespace(text.charAt(0))) text.deleteCharAt(0);
        while (text.length() > 0 && MString.isWhitespace(text.charAt(text.length() - 1)))
            text.deleteCharAt(text.length() - 1);
    }

    /**
     * @param tag
     * @return
     */
    private String getTagName(String tag) {

        if (MString.isIndex(tag, MString.WHITESPACE)) {
            return MString.beforeIndex(tag, MString.WHITESPACE);
        }
        return tag;
    }

    /** @param tag */
    private Hashtable<String, String> getTagParams(String tag) {

        Hashtable<String, String> out = new Hashtable<String, String>();

        if (!MString.isIndex(tag, MString.WHITESPACE)) {
            return out;
        }

        String tagx = MString.afterIndex(tag, MString.WHITESPACE);

        while (tagx.length() != 0) {
            int pos = tagx.indexOf('=');
            if (pos < 0) return out;

            String key = tagx.substring(0, pos).trim();
            tagx = tagx.substring(pos + 1);

            int pos1 = tagx.indexOf('"');
            int pos2 = tagx.indexOf('\'');

            if (pos1 < 0) pos = pos2;
            else if (pos2 < 0) pos = pos1;
            else if (pos2 < pos1) pos = pos2;
            else pos = pos1;

            if (pos < 0) return out;
            tagx = tagx.substring(pos + 1);

            pos = tagx.indexOf(pos == pos1 ? '"' : '\'');
            if (pos < 0) return out;
            out.put(MXml.decode(key), MXml.decode(tagx.substring(0, pos)));
            tagx = tagx.substring(pos + 1);
        }

        return out;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    public interface Listener {

        public boolean foundText(String text);

        public boolean foundNote(String note);

        public boolean foundProcessorInstruction(String pi);

        public boolean foundTagClose(String name);

        public boolean foundSingleTag(String name, Map<String, String> _params);

        public boolean foundTagOpen(String name, Map<String, String> _params);
    }
}
