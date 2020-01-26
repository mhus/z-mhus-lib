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

public class CsvStringParser extends StringTokenizerParser {

    public CsvStringParser(String condition) {
        super(condition);
        breakableCharacters = ",";
        enclosureCharacters = "\"";
        whiteSpace = " \r";
        encapsulateCharacters = "\"";
        lineSeparator = "\n";
    }
}
