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
package de.mhus.lib.core.logging;

import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.cfg.CfgInt;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.util.NullValue;

/**
 * Use this tool to shrink properties.
 *
 * @author mikehummel
 */
public class LogProperties extends MProperties {

    private static CfgInt CFG_MAX_STRING_SIZE =
            new CfgInt(LogProperties.class, "maxStringSize", 50);
    private static CfgString CFG_IGNORE = new CfgString(LogProperties.class, "ignore", null);
    private static CfgString CFG_STRING_SIZE =
            new CfgString(LogProperties.class, "stringSize", null);

    public LogProperties(Map<String, Object> parameters) {
        super(parameters);

        String[] ignore = CFG_IGNORE.value() == null ? null : CFG_IGNORE.value().split(",");
        String[] stringSize =
                CFG_STRING_SIZE.value() == null ? null : CFG_STRING_SIZE.value().split(",");

        for (String key : new LinkedList<>(parameters.keySet())) {
            if (ignore != null) {
                for (String pattern : ignore) {
                    if (key.matches(pattern)) {
                        remove(key);
                        continue;
                    }
                }
            }
            Object val = get(key);
            if (val == null || val instanceof NullValue) continue;
            if (val instanceof String) {
                if (stringSize != null) {
                    for (String pattern : stringSize) {
                        if (key.matches(pattern)) {
                            put(key, "*** size: " + ((String) val).length());
                        }
                    }
                }
                if (((String) val).length() > CFG_MAX_STRING_SIZE.value())
                    put(key, ((String) val).substring(0, CFG_MAX_STRING_SIZE.value()) + "...");
            }
        }
    }
}
