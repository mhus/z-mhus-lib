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
package de.mhus.lib.core.util;

public class Lorem {

    public static String create() {
        StringBuilder out = new StringBuilder();
        int c = (int) (Math.random() * 10d) + 1;
        for (int i = 0; i < c; i++)
            out.append(
                    "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ");
        return out.toString().trim();
    }

    public static String create(int paragraphs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paragraphs; i++) sb.append(create()).append("\n\n");
        return sb.toString();
    }

    public static String createHtml(int paragraphs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paragraphs; i++) sb.append("<p>").append(create()).append("</p>\n");
        return sb.toString();
    }
}
