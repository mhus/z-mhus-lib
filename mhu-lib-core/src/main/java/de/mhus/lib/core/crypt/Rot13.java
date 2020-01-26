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
package de.mhus.lib.core.crypt;

public class Rot13 {

    public static String encode(String in) {
        return decode(in);
    }

    public static String decode(String in) {

        StringBuilder out = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {
            int chr = in.charAt(i);

            // convert char if required
            if ((chr >= 'A') && (chr <= 'Z')) {
                chr += 13;
                if (chr > 'Z') chr -= 26;
            } else if ((chr >= 'a') && (chr <= 'z')) {
                chr += 13;
                if (chr > 'z') chr -= 26;
            } else if ((chr >= '0') && (chr <= '9')) {
                chr += 5;
                if (chr > '9') chr -= 10;
            }

            // and return it to sender
            out.append((char) chr);
        }

        return out.toString();
    }
}
