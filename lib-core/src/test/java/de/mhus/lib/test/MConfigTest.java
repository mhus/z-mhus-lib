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
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MLogFactory;
import de.mhus.lib.core.mapi.IApiInternal;
import de.mhus.lib.core.mapi.MCfgManager;
import de.mhus.lib.errors.MException;

public class MConfigTest {

    private static String initiatorValue;

    @Test
    public void testLoading() throws MException {
        
        // init first as 'dummy'
        MApi.get().getCfgManager();
        
        
        System.setProperty(MConstants.PROP_CONFIG_FILE, "src/test/resources/de/mhus/lib/test/mhus-config.xml");
        
        IApiInternal internal = new IApiInternal() {
            
            @Override
            public void setMLogFactory(MLogFactory mlogFactory) {
            }
            
            @Override
            public void setLogFactory(LogFactory logFactory) {
            }
            
            @Override
            public void setBaseDir(File file) {
            }
            
            @Override
            public Set<String> getLogTrace() {
                return new HashSet<>();
            }
        };
        
        initiatorValue = "";
        MCfgManager manager = new MCfgManager(internal );
        assertEquals("", initiatorValue);
        
        IConfig systemC = manager.getCfg("system");
        assertNotNull(systemC);
        IConfig testC = manager.getCfg("de.test");
        assertNotNull(testC);
        
        initiatorValue = "";
        manager.startInitiators();
        assertEquals("abcdefghi", initiatorValue);
        
    }

    public static void initiate(IConfig config) {
        System.out.println("Initiate: "+config);
        initiatorValue = initiatorValue + config.getString("value", null);
    }
}
