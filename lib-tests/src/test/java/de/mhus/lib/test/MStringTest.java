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
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.errors.MException;
import de.mhus.lib.tests.TestCase;

public class MStringTest extends TestCase {

    @Test
    public void testSubstr() throws MException {
        String t = "abcdefghijklmnopqrstuvwxyz";
        // standards
        {
            String v = MString.substr(t, 0, 0);
            assertEquals("", v);
        }
        {
            String v = MString.substr(t, 0, 26);
            assertEquals("abcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substr(t, 0, 25);
            assertEquals("abcdefghijklmnopqrstuvwxy", v);
        }
        {
            String v = MString.substr(t, 1, 25);
            assertEquals("bcdefghijklmnopqrstuvwxy", v);
        }

        // exceptions
        {
            String v = MString.substr(t, 0, 30);
            assertEquals("abcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substr(t, 1, 30);
            assertEquals("bcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substr(t, 10, 5);
            assertEquals("", v);
        }
        {
            String v = MString.substr(null, 1, 5);
            assertEquals("", v);
        }
        {
            String v = MString.substr(t, 30, 35);
            assertEquals("", v);
        }

        // from right to left - only in ext version
        {
            String v = MString.substr(t, -3, 30);
            assertEquals("abcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substr(t, -3, -1);
            assertEquals("", v);
        }
        {
            String v = MString.substr(t, 0, -1);
            assertEquals("", v);
        }
        {
            String v = MString.substr(t, 1, -2);
            assertEquals("", v);
        }
        {
            String v = MString.substr(t, -1, 2);
            assertEquals("ab", v);
        }
    }

    @Test
    public void testSubstrExt() throws MException {
        String t = "abcdefghijklmnopqrstuvwxyz";
        // standards
        {
            String v = MString.substrext(t, 0, 0);
            assertEquals("", v);
        }
        {
            String v = MString.substrext(t, 0, 26);
            assertEquals("abcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substrext(t, 0, 25);
            assertEquals("abcdefghijklmnopqrstuvwxy", v);
        }
        {
            String v = MString.substrext(t, 1, 25);
            assertEquals("bcdefghijklmnopqrstuvwxy", v);
        }

        // exceptions
        {
            String v = MString.substrext(t, 0, 30);
            assertEquals("abcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substrext(t, 1, 30);
            assertEquals("bcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substrext(t, 10, 5);
            assertEquals("", v);
        }
        {
            String v = MString.substrext(null, 1, 5);
            assertEquals("", v);
        }
        {
            String v = MString.substrext(t, 30, 35);
            assertEquals("", v);
        }

        // from right to left
        {
            String v = MString.substrext(t, -3, 30);
            assertEquals("yz", v);
        }
        {
            String v = MString.substrext(t, -3, -1);
            assertEquals("yz", v);
        }
        {
            String v = MString.substrext(t, 0, -1);
            assertEquals("abcdefghijklmnopqrstuvwxyz", v);
        }
        {
            String v = MString.substrext(t, 1, -2);
            assertEquals("bcdefghijklmnopqrstuvwxy", v);
        }
        {
            String v = MString.substrext(t, -1, 2);
            assertEquals("", v);
        }
    }

    @Test
    public void testStringCompiler() throws MException {
        MProperties attr = new MProperties();
        attr.setString("name", "Mickey");
        {
            String pattern = "Name $name$ end";
            String str = StringCompiler.compile(pattern).execute(attr);
            assertEquals("Name Mickey end", str);
        }
        {
            String pattern = "Name ${name} end";
            String str = StringCompiler.compile(pattern).execute(attr);
            assertEquals("Name Mickey end", str);
        }
        {
            String pattern = "Name $none:Mickey$ end";
            String str = StringCompiler.compile(pattern).execute(attr);
            assertEquals("Name Mickey end", str);
        }
        {
            String pattern = "Name ${none:Mickey} end";
            String str = StringCompiler.compile(pattern).execute(attr);
            assertEquals("Name Mickey end", str);
        }
        {
            String pattern = "Name $name:Other$ end";
            String str = StringCompiler.compile(pattern).execute(attr);
            assertEquals("Name Mickey end", str);
        }
    }

    @Test
    public void testAscii127() {
        for (Entry<String, String> map : MString.ASCII127_MAPPING.entrySet()) {
            if (map.getKey().length() == 1)
                assertEquals(
                        MString.rep(map.getKey().charAt(0), map.getValue().length()),
                        MString.toAscii127(map.getValue()));
            else assertEquals(map.getKey(), MString.toAscii127(map.getValue()));
        }
        for (Entry<String, String> map : MString.ASCII127_MAPPING.entrySet()) {
            if (map.getKey().length() == 1)
                assertEquals(
                        MString.rep(
                                Character.toUpperCase(map.getKey().charAt(0)),
                                map.getValue().length()),
                        MString.toAscii127(map.getValue().toUpperCase()));
            else if (!map.getKey()
                    .equals("ss")) // ignore this one, there is no upper key for it - but should be
            assertEquals(
                        map.getKey().substring(0, 1).toUpperCase() + map.getKey().substring(1),
                        MString.toAscii127(map.getValue().toUpperCase()));
        }

        assertEquals(
                "Voila ce a quoi elle ressemblera a la fin, mais pour le moment, c'est le chaos total !",
                MString.toAscii127(
                        "Voil\u00e0 ce \u00e0 quoi elle ressemblera \u00e0 la fin, mais pour le moment, c'est le chaos total !"));
        assertEquals(
                "Vrc, vstrc prst skrz stvrthrst, chrt.",
                MString.toAscii127(
                        "Vr\u010d, vstr\u010d prst skrz \u0161tvr\u0165hrs\u0165, chrt."));
        assertEquals(
                "a a a b c c d d dz dz e e f g h ch i i j k l l l m n n o o o p q r r s s t t u u v w x y y z z",
                MString.toAscii127(
                        "a \u00e1 \u00e4 b c \u010d d \u010f dz d\u017e e \u00e9 f g h ch i \u00ed j k l \u013a \u013e m n \u0148 o \u00f3 \u00f4 p q r \u0155 s \u0161 t \u0165 u \u00fa v w x y \u00fd z \u017e"));
        assertEquals(
                "There is nothing to change.", MString.toAscii127("There is nothing to change."));
        assertEquals(
                "Wodka rano i wieczorem dla kazdego ojca jest lepiej niz zajac sie bachorem.",
                MString.toAscii127(
                        "W\u00f3dka rano i wieczorem dla ka\u017cdego ojca jest lepiej ni\u017c zaj\u0105c si\u0119 bachorem."));
    }

    @Test
    public void testInitials() {
        String title = " ";
        String res = MString.findInitials(title, true, true, 0);
        assertEquals("?", res);

        title = " aber Hallo , wer hat denn hier 51 Hasen.";

        res = MString.findInitials(title, true, false, 0);
        assertEquals("HH", res);

        res = MString.findInitials(title, false, false, 0);
        assertEquals("aHwhdhH", res);

        res = MString.findInitials(title, false, false, 3);
        assertEquals("aHw", res);

        res = MString.findInitials(title, false, true, 0);
        assertEquals("aHwhdh5H", res);
    }

    @Test
    public void testEncodeUnicode() {
        {
            String test = "Herr Müller";
            String code = MString.encodeUnicode(test);
            System.out.println(code);
            assertEquals("Herr M\\u00fcller", code);
            String deco = MString.decodeUnicode(code);
            assertEquals(test, deco);
        }

        {
            String test = "Herr Müller\nFrau Müller";
            String code = MString.encodeUnicode(test);
            System.out.println(code);
            assertEquals("Herr M\\u00fcller\nFrau M\\u00fcller", code);
            String deco = MString.decodeUnicode(code);
            assertEquals(test, deco);
        }

        {
            String test = "Herr Müller\nFrau Müller";
            String code = MString.encodeUnicode(test, true);
            System.out.println(code);
            assertEquals("Herr M\\u00fcller\\u000aFrau M\\u00fcller", code);
            String deco = MString.decodeUnicode(code);
            assertEquals(test, deco);
        }
    }

    @Test
    public void testTruncateNice() {
        {
            String test = "abcdefghijklmnopqrstuvwxyz";
            String res = MString.truncateNice(test, 10);
            assertEquals(10, res.length());
            assertEquals("ab...vwxyz", res);
        }
        {
            String test = "abcdefghijklmnopqrstuvwxyz";
            String res = MString.truncateNice(test, 10, 1);
            assertEquals(10, res.length());
            assertEquals("a...uvwxyz", res);
        }
        {
            String test = "abcdefghijklmnopqrstuvwxyz";
            String res = MString.truncateNice(test, 10, -1);
            assertEquals(10, res.length());
            assertEquals("abcdefg...", res);
        }
        {
            String test = "abcdefghijklmnopqrstuvwxyz";
            String res = MString.truncateNice(test, 10, -4);
            assertEquals(10, res.length());
            assertEquals("abcdef...z", res);
        }
    }
}
