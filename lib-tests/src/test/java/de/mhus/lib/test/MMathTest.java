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

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MBigMath;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.util.Base64;
import de.mhus.lib.tests.TestCase;

public class MMathTest extends TestCase {

    @Test
    public void testTrimDecimals() {
        double in = 345.1234567;

        {
            double out = MMath.truncateDecimals(in, 0);
            assertEquals(345.0, out);
        }
        {
            double out = MMath.truncateDecimals(in, 1);
            assertEquals(345.1, out);
        }
        {
            double out = MMath.truncateDecimals(in, 2);
            assertEquals(345.12, out);
        }
        {
            double out = MMath.truncateDecimals(in, 3);
            assertEquals(345.123, out);
        }
        {
            double out = MMath.truncateDecimals(in, 4);
            assertEquals(345.1234, out);
        }
        {
            double out = MMath.truncateDecimals(in, 5);
            assertEquals(345.12345, out);
        }
    }

    @Test
    public void testByteAddRotate() {
        for (byte d = Byte.MIN_VALUE; d < Byte.MAX_VALUE; d++) {
            for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
                byte l = MMath.addRotate(b, d);
                byte r = MMath.subRotate(l, d);
                if (b != r) {
                    System.out.println(b + " -> " + l + " -> " + r);
                    System.out.println(
                            MCast.toBitsString(b)
                                    + " -> "
                                    + MCast.toBitsString(l)
                                    + " -> "
                                    + MCast.toBitsString(r));
                }
                assertEquals(b, r);
            }
        }
    }

    @Test
    public void testByteRotate() {
        for (int d = 1; d < 8; d++) {
            for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
                byte l = MMath.rotl(b, d);
                byte r = MMath.rotr(l, d);
                if (b != r) {
                    System.out.println(b + " -> " + l + " -> " + r);
                    System.out.println(
                            MCast.toBitsString(b)
                                    + " -> "
                                    + MCast.toBitsString(l)
                                    + " -> "
                                    + MCast.toBitsString(r));
                }
                assertEquals(b, r);
            }
        }
    }

    @Test
    public void testIntRotate() {
        for (int d = 1; d < 32; d++) {
            {
                int i = Integer.MIN_VALUE;
                int l = MMath.rotl(i, d);
                int r = MMath.rotr(l, d);
                if (i != r) System.out.println(i + " -> " + l + " -> " + r);
                assertEquals(i, r);
            }
            {
                int i = Integer.MAX_VALUE;
                int l = MMath.rotl(i, d);
                int r = MMath.rotr(l, d);
                if (i != r) System.out.println(i + " -> " + l + " -> " + r);
                assertEquals(i, r);
            }
            {
                int i = 1;
                int l = MMath.rotl(i, d);
                int r = MMath.rotr(l, d);
                if (i != r) System.out.println(i + " -> " + l + " -> " + r);
                assertEquals(i, r);
            }
            {
                int i = -1;
                int l = MMath.rotl(i, d);
                int r = MMath.rotr(l, d);
                if (i != r) System.out.println(i + " -> " + l + " -> " + r);
                assertEquals(i, r);
            }
            {
                int i = 0;
                int l = MMath.rotl(i, d);
                int r = MMath.rotr(l, d);
                if (i != r) System.out.println(i + " -> " + l + " -> " + r);
                assertEquals(i, r);
            }
        }
    }

    @Test
    public void testBigMath() {
        BigDecimal a = BigDecimal.valueOf(10);
        BigDecimal b = BigDecimal.valueOf(20);

        assertEquals(a, MBigMath.min(a, b));
        assertEquals(b, MBigMath.max(a, b));
    }

    @Test
    public void testBase64Uuid() {
        for (int i = 0; i < 10; i++) {
            UUID id = UUID.randomUUID();
            String base = Base64.uuidToBase64(id);
            System.out.println("UUID Compress: " + id + " to " + base + " " + base.length());
            UUID id2 = Base64.base64ToUuid(base);
            assertEquals(id, id2);
        }
    }
}
