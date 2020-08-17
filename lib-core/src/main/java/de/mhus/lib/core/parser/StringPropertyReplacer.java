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
package de.mhus.lib.core.parser;

/**
 * This class will search and replace properties in the format like ${key}
 *
 * @author mikehummel
 */
public abstract class StringPropertyReplacer {

    public String process(String in) {
        StringBuilder out = null;
        while (true) {
            int p = in.indexOf("${");
            if (p < 0) break;
            if (out == null) out = new StringBuilder();
            out.append(in.substring(0, p));
            int p2 = in.indexOf('}', p);
            if (p2 < 0) break;
            String key = in.substring(p + 2, p2);
            String val = findValueFor(key);
            if (val != null) out.append(val);
            else out.append("${").append(key).append("}");

            // reduce 'in'
            in = in.substring(p2 + 1);
        }

        if (out != null) {
            out.append(in);
            return out.toString();
        }

        return in;
    }

    public abstract String findValueFor(String key);
}
