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
import java.util.HashMap;
import java.util.Map;

import de.mhus.lib.core.util.MNls;

public class GermanHolidays extends Holidays {

    // TODO The list is not complete

    @SuppressWarnings("deprecation")
    @Override
    public Map<Date, String> getHolidays(MNls nls, int year, String regionHint) {
        if (nls == null) nls = new MNls();
        if (regionHint == null) regionHint = "";

        regionHint = regionHint.toLowerCase();
        boolean evangelic =
                regionHint.equals("evangelical")
                        || regionHint.equals("berlin")
                        || regionHint.equals("brandenburg")
                        || regionHint.equals("bremen")
                        || regionHint.equals("hamburg")
                        || regionHint.equals("hessen")
                        || regionHint.equals("mecklenburg-vorpommern")
                        || regionHint.equals("mecklenburg")
                        || regionHint.equals("vorpommern")
                        || regionHint.equals("niedersachsen")
                        || regionHint.equals("sachsen")
                        || regionHint.equals("sachsen-anhalt")
                        || regionHint.equals("schleswig-holstein")
                        || regionHint.equals("thüringen");

        Map<Date, String> out = new HashMap<>();

        // fix days

        out.put(new Date(year - 1900, 0, 1), nls.find("newYearsDay=New Years Day"));
        out.put(new Date(year - 1900, 4, 1), nls.find("firstMay=1. May"));

        out.put(new Date(year - 1900, 9, 3), nls.find("germanUnity=Day of German Unity"));

        if (evangelic)
            out.put(new Date(year - 1900, 9, 31), nls.find("reformationDay=Reformation Day"));
        else out.put(new Date(year - 1900, 10, 1), nls.find("allSaintsDay=All Saints' Day"));

        out.put(new Date(year - 1900, 11, 25), nls.find("xmax1=1. Christmas Holiday"));
        out.put(new Date(year - 1900, 11, 26), nls.find("xmax2=2. Christmas Holiday"));

        // --- Christian holidays

        Calendar cal = getEasterSundayDate(year);
        out.put(cal.getTime(), nls.find("easterSunday=Easter Sunday"));
        cal.add(Calendar.DAY_OF_MONTH, -2);
        out.put(cal.getTime(), nls.find("goodFriday=Good Friday"));
        cal.add(Calendar.DAY_OF_MONTH, 3);
        out.put(cal.getTime(), nls.find("easterMonday=Easter Monday"));

        cal.add(Calendar.DAY_OF_MONTH, 7 * 7);
        out.put(cal.getTime(), nls.find("whitsunMonday=Whitsun Monday"));
        cal.add(Calendar.DAY_OF_MONTH, -1);
        out.put(cal.getTime(), nls.find("whitsunSunday=Whitsun Sunday"));

        cal.add(Calendar.DAY_OF_MONTH, -10);
        out.put(cal.getTime(), nls.find("ascensionDay=Ascension Day"));

        return out;
    }
}
