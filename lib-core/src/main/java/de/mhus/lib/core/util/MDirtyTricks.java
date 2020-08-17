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

import java.lang.reflect.Field;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.cfg.CfgValue;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.mapi.IApiInternal;
import de.mhus.lib.logging.JavaLoggerFactory;

// This class is for use with JUnit tests or similar
public class MDirtyTricks {

    public static boolean updateCfgValue(Class<?> owner, String path, String value) {
        for (CfgValue<?> cfg : MApi.getCfgUpdater().getList()) {
            if (cfg.getOwner().equals(owner.getCanonicalName()) && cfg.getPath().equals(path)) {
                cfg.setValue(value);
                return true;
            }
        }
        return false;
    }

    public static void setTestLogging() {
        try {
            MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MApi.setDirtyTrace(true);
    }

    public static void setJavaLogging() {
        ((IApiInternal) MApi.get()).setLogFactory(new JavaLoggerFactory());
    }

    public static void cleanupMApi()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException,
                    IllegalAccessException {
        Field field = MApi.class.getDeclaredField("api");
        if (!field.canAccess(null)) field.setAccessible(true);
        field.set(null, null);
    }
}
