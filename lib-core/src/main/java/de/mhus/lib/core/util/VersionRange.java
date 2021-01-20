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
package de.mhus.lib.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.mhus.lib.basics.Versioned;

/*
 * From aQute.libg.version.VersionRange
 */
public class VersionRange {
    Version high;
    Version low;
    char start = '[';
    char end = ']';
    public static final String VERSION_STRING =
            "(\\d+)(\\.(\\d+)(\\.(\\d+)(\\.([-_\\da-zA-Z]+))?)?)?";

    static Pattern RANGE =
            Pattern.compile(
                    "(\\(|\\[)\\s*("
                            + VERSION_STRING
                            + ")\\s*,\\s*("
                            + VERSION_STRING
                            + ")\\s*(\\)|\\])");

    public VersionRange(String string) {
        string = string.trim();
        if (string.indexOf(',') > 0) {
            if (string.startsWith("]")) // ]1.0.0,...
            string = "(" + string.substring(1);
            else if (!string.startsWith("[") && !string.startsWith("(")) string = "[" + string;

            if (string.endsWith("[")) // ...,2.0.0[
            string = string.substring(0, string.length() - 1) + ")";
            else if (!string.endsWith("]") && !string.endsWith(")")) string = string + ")";
        } else if (string.endsWith("+")) {
            string = string.substring(0, string.length() - 1);
            Version v = new Version(string);
            Version next = v.nextMajor();
            string = "[" + v.withoutSuffix() + "," + next + ")";
        }

        Matcher m = RANGE.matcher(string);
        if (m.matches()) {
            start = m.group(1).charAt(0);
            String v1 = m.group(2);
            String v2 = m.group(10);
            low = new Version(v1);
            high = new Version(v2);
            end = m.group(18).charAt(0);
            if (low.compareTo(high) > 0)
                throw new IllegalArgumentException(
                        "Low Range is higher than High Range: " + low + "-" + high);

        } else high = low = new Version(string);
    }

    public boolean isRange() {
        return high != low;
    }

    public boolean includeLow() {
        return start == '[';
    }

    public boolean includeHigh() {
        return end == ']';
    }

    @Override
    public String toString() {
        if (high == low) return high.toString();

        StringBuilder sb = new StringBuilder();
        sb.append(start);
        sb.append(low);
        sb.append(',');
        sb.append(high);
        sb.append(end);
        return sb.toString();
    }

    public Version getLow() {
        return low;
    }

    public Version getHigh() {
        return high;
    }

    public boolean includes(Versioned v) {
        return includes(new Version(v.getVersionString()));
    }

    public boolean includes(Version v) {
        if (!isRange()) {
            return low.compareTo(v) == 0; // need exact match
        }
        if (includeLow()) {
            if (v.compareTo(low) < 0) return false;
        } else if (v.compareTo(low) <= 0) return false;

        if (includeHigh()) {
            if (v.compareTo(high) > 0) return false;
        } else if (v.compareTo(high) >= 0) return false;

        return true;
    }
}
