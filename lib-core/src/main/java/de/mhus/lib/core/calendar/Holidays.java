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
package de.mhus.lib.core.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.mhus.lib.core.util.MNls;

public abstract class Holidays {

    public static Calendar getEasterSundayDate(int year) {
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7,
                n = (h - m + r + 90) / 25,
                p = (h - m + r + n + 19) % 32;

        Calendar out = Calendar.getInstance();
        out.setTimeInMillis(0);
        out.set(Calendar.HOUR_OF_DAY, 0);

        out.set(Calendar.YEAR, year);
        out.set(Calendar.MONTH, n - 1);
        out.set(Calendar.DAY_OF_MONTH, p);

        return out;
    }

    /**
     * Return a array with three elements 0: day of month, start at 1 1: month, start with 1 2:
     * year, as provided
     *
     * @param year
     * @return The date as array
     */
    public static int[] getEasterSunday(int year) {
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7,
                n = (h - m + r + 90) / 25,
                p = (h - m + r + n + 19) % 32;

        return new int[] {p, n, year};
    }

    /**
     * Return a list of national holidays for the year.
     *
     * @param nls The nls or null for default
     * @param year The year to provide holidays
     * @param regionHint Region hint or null
     * @return List of holidays for the year, key, name of the holiday, value the date in the year.
     */
    public abstract Map<Date, String> getHolidays(MNls nls, int year, String regionHint);

    public static Holidays getHolidaysForLocale(Locale locale) {
        if (locale == null) return null;
        if (locale.getCountry().equals("DE")) return new GermanHolidays();
        return null;
    }
}
