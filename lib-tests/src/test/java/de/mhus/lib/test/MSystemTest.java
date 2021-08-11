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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;
import java.util.jar.Manifest;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.test.util.StringValue;
import de.mhus.lib.test.util.Template;
import de.mhus.lib.tests.TestCase;

public class MSystemTest extends TestCase {

    @Test
    public void testManifest() throws NotFoundException {
        Manifest manifest = MSystem.getManifest(MSystem.class);
        assertNotNull(manifest);
        String manifestVersion = manifest.getMainAttributes().getValue("Manifest-Version");
        System.out.println(manifestVersion);
        assertEquals("1.0", manifestVersion);
    }

    @Test
    public void testTemplateNames() {
        Class<?> testy = StringValue.class;

        assertEquals("java.lang.String", MSystem.getTemplateCanonicalName(testy, 0));
        assertEquals(
                "java.lang.Integer",
                MSystem.getTemplateCanonicalName((new Template<Integer>() {}).getClass(), 0));

        assertNull(MSystem.getTemplateCanonicalName(testy, 1));
        assertNull(MSystem.getTemplateCanonicalName(new Template<Integer>().getClass(), 0));
        assertNull(MSystem.getTemplateCanonicalName(String.class, 0));
    }

    @Test
    public void testCanonicalClassNames() {
        {
            String name = MSystem.getCanonicalClassName(String.class);
            assertEquals("java.lang.String", name);
        }
        {
            String name = MSystem.getCanonicalClassName(Map.Entry.class);
            assertEquals("java.util.Map.Entry", name);
        }
        {
            String name =
                    MSystem.getCanonicalClassName(
                            new Runnable() {
                                @Override
                                public void run() {}
                            }.getClass());
            assertEquals("de.mhus.lib.test.MSystemTest$2", name);
        }
    }
}
