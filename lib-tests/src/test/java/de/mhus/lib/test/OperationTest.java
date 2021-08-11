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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.util.MapValue;
import de.mhus.lib.core.operation.util.SuccessfulForceMap;
import de.mhus.lib.tests.TestCase;

public class OperationTest extends TestCase {

    @Test
    @SuppressWarnings("deprecation")
    public void testForceMapResult1() {
        SuccessfulForceMap res = new SuccessfulForceMap("path", "msg", 0);
        res.put("key", "value");
        System.out.println(res);
        assertTrue(res.getResult() instanceof MapValue);
        assertNotNull(((MapValue) res.getResult()).getValue());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testForceMapResult2() {
        Operation oper = new TestOperation();
        SuccessfulForceMap res = new SuccessfulForceMap(oper, "msg");
        res.put("key", "value");
        System.out.println(res);
        assertTrue(res.getResult() instanceof MapValue);
        assertNotNull(((MapValue) res.getResult()).getValue());
    }
}
