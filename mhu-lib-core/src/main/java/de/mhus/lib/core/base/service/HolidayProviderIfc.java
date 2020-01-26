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
package de.mhus.lib.core.base.service;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(HolidayProviderImpl.class)
public interface HolidayProviderIfc {

    /**
     * Returns if the given day is a holiday in the given area.
     *
     * @param locale The requested area or null for the default area.
     * @param date The day to check
     * @return true if the day (ignoring time) is a holiday.
     */
    boolean isHoliday(Locale locale, Date date);

    /**
     * Returns true if the day is not a Sunday and not a holiday.
     *
     * @param locale
     * @param date
     * @return true if the specified date is a working day
     */
    boolean isWorkingDay(Locale locale, Date date);

    /**
     * Returns a list of holidays for the area and year.
     *
     * @param locale
     * @param year
     * @return true if the specified date is a holiday
     */
    Map<String, String> getHolidays(Locale locale, int year);
}
