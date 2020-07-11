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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MSql;
import de.mhus.lib.core.MValidator;
import de.mhus.lib.tests.TestCase;

public class MValidatorTest extends TestCase {

    @Test
    public void testFileName() {
        assertEquals(true, MValidator.isPosixFileName("test123"));
        assertEquals(true, MValidator.isPosixFileName("test123_test-543.dotsep"));
        assertEquals(true, MValidator.isPosixFileName("test"));

        assertEquals(false, MValidator.isPosixFileName("test|it"));
        assertEquals(false, MValidator.isPosixFileName("test it"));
        assertEquals(false, MValidator.isPosixFileName("test&it"));
        assertEquals(false, MValidator.isPosixFileName("test/it"));

        assertEquals(true, MValidator.isPosixFilePath("/path/test123"));
        assertEquals(true, MValidator.isPosixFilePath("/path/test123_test-543.dotsep"));
        assertEquals(true, MValidator.isPosixFilePath("/path/test"));

        assertEquals(true, MValidator.isFilePath("C:/path/test"));
        assertEquals(true, MValidator.isFilePath("C:\\path\\test"));

        assertEquals(false, MValidator.isPosixFilePath("/path/test|it"));
        assertEquals(false, MValidator.isPosixFilePath("/path/test it"));
        assertEquals(false, MValidator.isPosixFilePath("/path/test&it"));
        assertEquals(false, MValidator.isPosixFilePath("~/path/test&it"));

        assertEquals(false, MValidator.isPosixFilePath("C:/path/test"));
        assertEquals(false, MValidator.isPosixFilePath("C:\\path\\test"));
    }

    @Test
    public void testEMail() {
        assertEquals(false, MValidator.isEmailAddress(null));
        assertEquals(false, MValidator.isEmailAddress(""));
        assertEquals(false, MValidator.isEmailAddress("@mhus.de"));
        assertEquals(false, MValidator.isEmailAddress("mike@"));
        assertEquals(true, MValidator.isEmailAddress("mike@mhus.de"));
        assertEquals(false, MValidator.isEmailAddress("mike@alababa"));
        assertEquals(true, MValidator.isEmailAddress("mike@alababa.blabla.de"));
        assertEquals(true, MValidator.isEmailAddress("mike@alababa.aha.soso.local"));
    }

    @Test
    public void testZip() {
        Locale l = new Locale("de", "DE");
        assertEquals(true, MValidator.isZipCode(l, "04212"));
        assertEquals(true, MValidator.isZipCode(l, "95030"));
        assertEquals(false, MValidator.isZipCode(l, "00999"));
        assertEquals(false, MValidator.isZipCode(l, "100000"));
        assertEquals(false, MValidator.isZipCode(l, "2000"));
        l = new Locale("en", "US");
        try {
            MValidator.isZipCode(l, "04212");
            assertEquals(true, false);
        } catch (Throwable t) {
        }
    }

    @Test
    public void testNames() {
        assertEquals(true, MValidator.isFirstName("Güven"));
        assertEquals(true, MValidator.isFirstName("André"));
        assertEquals(true, MValidator.isLastName("Müller"));
    }

    @Test
    public void testPhone() {
        assertEquals(true, MValidator.isPhoneNumber("+49 40 43214"));
        assertEquals(true, MValidator.isPhoneNumber("040-43214"));
        assertEquals(false, MValidator.isPhoneNumber("+49 40 abc"));
        assertEquals(false, MValidator.isPhoneNumber("+49"));
        assertEquals(false, MValidator.isPhoneNumber("+49 40 12345 12345 12345 12345"));
        assertEquals(true, MValidator.isPhoneNumber("+49-40/1234"));
        assertEquals(false, MValidator.isPhoneNumber("+49(0)40/1234"));

        assertEquals(true, MValidator.isPhoneNumber("+49-40/1234", Locale.GERMAN));
        assertEquals(true, MValidator.isPhoneNumber("+49-40/1234", Locale.GERMANY));

        assertEquals(true, MValidator.isPhoneNumber("(123) 123-1234", Locale.US));
        assertEquals(true, MValidator.isPhoneNumber("123-123-1234", Locale.US));
        assertEquals(false, MValidator.isPhoneNumber("+49-40/1234", Locale.US));
    }

    @Test
    public void testSqlColumnName() throws SQLException {
        MSql.column("_servus_mr_nobody");
        MSql.column("_servus_mr_nobody0");
        MSql.column("_Servus_Mr_Nobody");
        MSql.column("table.servus_mr_nobody");
        try {
            MSql.column("_servus_mr_nobody;");
            assertEquals(true, false);
        } catch (SQLException e) {

        }
        try {
            MSql.column("0_servus_mr_nobody");
            assertEquals(true, false);
        } catch (SQLException e) {

        }
        try {
            MSql.column("._servus_mr_nobody");
            assertEquals(true, false);
        } catch (SQLException e) {

        }

        MSql.column("`table.servus_mr_nobody`");
        try {
            MSql.column("`_servus_mr_nobody");
            assertEquals(true, false);
        } catch (SQLException e) {
        }
        try {
            MSql.column("_servus_mr_nobody`");
            assertEquals(true, false);
        } catch (SQLException e) {
        }
    }

    @Test
    public void testPassword() {
        assertTrue(MValidator.isPassword("Hello4Everyone_", 8, true, true, "alf"));
        assertFalse(MValidator.isPassword("HelloEveryone_", 8, true, true, "alf"));
        assertFalse(MValidator.isPassword("Hello4Everyone", 8, true, true, "alf"));
        assertFalse(MValidator.isPassword("Hello4Alf_", 8, true, true, "alf"));
        assertFalse(MValidator.isPassword("asdf1234", 8, true, false));
    }

    @Test
    public void testAZ09() {
        assertTrue(
                MValidator.isAZ09(
                        "abcdefghijklmnopqrszuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
        assertFalse(
                MValidator.isAZ09(
                        "abcdefghijklmnopqrszuvwxyz.ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
        assertFalse(MValidator.isAZ09(null));
    }
}
