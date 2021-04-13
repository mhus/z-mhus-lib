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

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.M;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.util.MDirtyTricks;
import de.mhus.lib.errors.MException;
import de.mhus.lib.tests.TestCase;

public class MConfigTest extends TestCase {

    private static String initiatorValue;

    @Test
    public void testLoading()
            throws MException, NoSuchFieldException, SecurityException, IllegalArgumentException,
                    IllegalAccessException {

        MDirtyTricks.cleanupMApi();

        System.setProperty(
                M.PROP_CONFIG_FILE, "src/test/resources/de/mhus/lib/test/mhus-config.xml");

        initiatorValue = "";
        assertEquals("", initiatorValue);
        //  start init
        MApi.get().getCfgManager();

        assertEquals("abcdefghi", initiatorValue);
    }

    public static void initiate(IConfig config) {
        System.out.println("Initiate: " + config);
        initiatorValue = initiatorValue + config.getString("value", null);
    }
}
