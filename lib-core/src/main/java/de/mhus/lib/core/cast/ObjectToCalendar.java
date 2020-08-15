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
package de.mhus.lib.core.cast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;

/**
 * Accepted formats:
 *
 * <p>now, jetzt integer as timestamp <<date>>[[ |_|T]<<time>>] date: yyyy-mm-dd dd.mm.yyyy
 * dd/mm/yyyy mm/dd/yyyy + locale == US time: MM:HH:ss[.SSS][Z][zone] MM-HH-ss[.SSS][Z][zone] MM:HH
 * am/pm[Z][zone] Jan 1, 2000 1:00 am[Z][zone]
 *
 * <p>Currently Not Accepted:
 *
 * <p>1. Januar 2000 13:00:00
 *
 * @author mikehummel
 */
public class ObjectToCalendar implements Caster<Object, Calendar> {

    private static final Log log = Log.getLog(ObjectToCalendar.class);
    private static HashMap<String, Integer> monthCatalog = new HashMap<>();

    static {
        monthCatalog.put("jan", 0);
        monthCatalog.put("januar", 0);
        monthCatalog.put("january", 0);
        monthCatalog.put("feb", 1);
        monthCatalog.put("februar", 1);
        monthCatalog.put("february", 1);
        monthCatalog.put("mrz", 2);
        monthCatalog.put("march", 2);
        monthCatalog.put("märz", 2);
        monthCatalog.put("apr", 3);
        monthCatalog.put("april", 3);
        monthCatalog.put("mai", 4);
        monthCatalog.put("may", 4);
        monthCatalog.put("jun", 5);
        monthCatalog.put("juni", 5);
        monthCatalog.put("june", 5);
        monthCatalog.put("jul", 6);
        monthCatalog.put("juli", 6);
        monthCatalog.put("july", 6);
        monthCatalog.put("aug", 7);
        monthCatalog.put("august", 7);
        monthCatalog.put("sep", 8);
        monthCatalog.put("september", 8);
        monthCatalog.put("septembre", 8);
        monthCatalog.put("okt", 9);
        monthCatalog.put("oct", 9);
        monthCatalog.put("oktober", 9);
        monthCatalog.put("october", 9);
        monthCatalog.put("nov", 10);
        monthCatalog.put("november", 10);
        monthCatalog.put("dez", 11);
        monthCatalog.put("dec", 11);
        monthCatalog.put("dezember", 11);
        monthCatalog.put("december", 11);
    }

    @Override
    public Class<? extends Calendar> getToClass() {
        return Calendar.class;
    }

    @Override
    public Class<? extends Object> getFromClass() {
        return Object.class;
    }

    @Override
    public Calendar cast(Object in, Calendar def) {
        return cast(in, def, Locale.getDefault());
    }

    public Calendar cast(Object in, Calendar def, Locale locale) {
        if (in == null) return def;
        if (in instanceof Calendar) return (Calendar) in;
        if (in instanceof Date) {
            Calendar c = Calendar.getInstance();
            c.setTime((Date) in);
            return c;
        }
        try {
            String ins = MCast.toString(in);
            Calendar ret = toCalendar(ins, locale);
            if (ret == null) return def;
            return ret;
        } catch (Throwable t) {
            return def;
        }
    }

    public static Calendar toCalendar(String in, Locale locale) {
        if (in == null) return null;

        try {

            Calendar c = Calendar.getInstance();
            if (in.equals("now") || in.equals("jetzt")) {
                return c;
            }

            boolean retOk = false;
            c.clear();

            String date = in.trim();

            // check if date and time
            char sep = '?';
            if (MString.isIndex(date, '_')) sep = '_';
            else if (MString.isIndex(date, ' ')) sep = ' ';
            else if (MString.isIndex(date, 'T')) sep = 'T';

            // read DE: '1. Januar 2000 13:00:00'
            int spacePos = date.indexOf(' ');
            if (spacePos > 0 && spacePos < 4) {
                String part = date.substring(spacePos).trim();
                if (part.length() > 0 && MString.isAlphabeticalAscii(part.charAt(0))) {
                    String dayStr = date.substring(0, spacePos);
                    if (dayStr.endsWith(".")) dayStr = dayStr.substring(0, dayStr.length() - 1);
                    int day = MCast.toint(dayStr, 0);
                    if (day <= 0 || day > 31) return null;
                    spacePos = part.indexOf(' ');
                    String monthStr = part;
                    if (spacePos > 0) {
                        monthStr = part.substring(0, spacePos);
                        date = part.substring(spacePos);
                    } else date = "";
                    monthStr = monthStr.trim();
                    int month = toMonth(monthStr);
                    if (month < 0 || month > 11) return null;

                    date = date.trim();
                    int year = c.get(Calendar.YEAR);
                    if (date.length() > 0) {
                        spacePos = date.indexOf(' ');
                        String yearStr = date;
                        if (spacePos > 0) {
                            yearStr = date.substring(0, spacePos);
                            date = date.substring(spacePos);
                        } else date = "";
                        year = MCast.toint(yearStr, 0);
                        if (year <= 0) return null;
                    }
                    c.set(year, month, day);

                    date = date.trim();
                    if (date.length() > 0) {
                        parseTime(c, date);
                    }

                    return c;
                }
            }

            {
                // US Format: 'Jan 1, 2000 1:00 am'
                if (sep == ' ' && MString.isIndex(date, ',')) {
                    int p1 = date.indexOf(' ');
                    int p2 = date.indexOf(',');
                    try {
                        int month = toMonth(date.substring(0, p1));
                        int day = Integer.parseInt(date.substring(p1 + 1, p2).trim());
                        date = date.substring(p2 + 1).trim();
                        p1 = date.indexOf(' ');
                        int year = Integer.parseInt(date.substring(0, p1).trim());
                        c.set(year, month, day);

                        date = " " + date.substring(p1 + 1).trim(); // rest is time
                        sep = ' ';
                        retOk = true;
                    } catch (NumberFormatException e) {
                    }
                }
            }

            if (sep != '?') {
                // found also time ... parse it !
                String time = MString.afterIndex(date, sep);
                date = MString.beforeIndex(in, sep).trim();
                parseTime(c, time);
            }

            if (retOk) return c;

            // parse the date
            if (date.indexOf('-') > 0) {
                // technical time 2000.12.31
                String[] parts = date.split("-");

                if (parts.length == 3) {
                    int year = Integer.parseInt(parts[0]);
                    if (parts[0].length() == 2)
                        year = year + 2000; // will this lib life for 100 years ???
                    int month = toMonth(parts[1]);
                    int day = Integer.parseInt(parts[2]);
                    c.set(year, month, day);
                } else if (parts.length == 2) {
                    c.set(Calendar.MONTH, toMonth(parts[0]));
                    c.set(Calendar.DATE, Integer.parseInt(parts[1]));
                } else {
                    parts = date.split("\\.");
                    if (parts.length == 3) {
                        int year = Integer.parseInt(parts[2]);
                        if (parts[2].length() == 2)
                            year = year + 2000; // will this lib life for 100 years ???
                        int month = toMonth(parts[1]);
                        int day = Integer.parseInt(parts[0]);
                        c.set(year, month, day);
                    } else if (parts.length == 2) {
                        int year = Integer.parseInt(parts[1]);
                        if (parts[1].length() == 2)
                            year = year + 2000; // will this lib life for 100 years ???
                        int month = toMonth(parts[0]);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.YEAR, year);
                    } else {
                        parts = date.split("/");
                        if (parts.length == 3) {
                            if (Locale.US.equals(locale)) {
                                c.set(
                                        Integer.parseInt(parts[2]),
                                        toMonth(parts[0]),
                                        Integer.parseInt(parts[1]));
                            } else {
                                c.set(
                                        Integer.parseInt(parts[2]),
                                        toMonth(parts[1]),
                                        Integer.parseInt(parts[0]));
                            }
                        }
                        if (parts.length == 2) {
                            c.set(Calendar.MONTH, toMonth(parts[0]));
                            c.set(Calendar.YEAR, Integer.parseInt(parts[1]));
                        }
                    }
                }

                //			if (zone != null) {
                //				TimeZone tz = TimeZone.getTimeZone(zone);
                //				c.setTimeZone(tz);
                //			}

                return c;
            } else if (date.indexOf('.') > 0) {
                // german time 31.12.2000
                String[] parts = date.split("\\.");
                if (parts.length == 3) {
                    int year = Integer.parseInt(parts[2]);
                    if (parts[2].length() == 2)
                        year = year + 2000; // will this lib life for 100 years ???
                    int month = Integer.parseInt(parts[1]) - 1;
                    int day = Integer.parseInt(parts[0]);
                    c.set(year, month, day);
                    return c;
                } else if (parts.length == 2) {
                    int year = Integer.parseInt(parts[1]);
                    if (parts[1].length() == 2)
                        year = year + 2000; // will this lib life for 100 years ???
                    int month = Integer.parseInt(parts[0]) - 1;
                    c.set(year, month, 1);
                    return c;
                } else return null;
            } else if (date.indexOf('/') > 0) {
                String[] parts = date.split("/");
                if (Locale.US.equals(locale)) {
                    // US 12/31/2000
                    if (parts.length == 3) {
                        int year = Integer.parseInt(parts[2]);
                        if (parts[2].length() == 2)
                            year = year + 2000; // will this lib life for 100 years ???
                        int month = Integer.parseInt(parts[0]) - 1;
                        int day = Integer.parseInt(parts[1]);
                        c.set(year, month, day);
                        return c;
                    } else if (parts.length == 2) {
                        int year = Integer.parseInt(parts[1]);
                        if (parts[1].length() == 2)
                            year = year + 2000; // will this lib life for 100 years ???
                        int month = Integer.parseInt(parts[0]) - 1;
                        c.set(year, month, 1);
                        return c;
                    } else return null;
                } else {
                    // france or UK 31/12/2000
                    if (parts.length == 3) {
                        int year = Integer.parseInt(parts[2]);
                        if (parts[2].length() == 2)
                            year = year + 2000; // will this lib life for 100 years ???
                        int month = Integer.parseInt(parts[1]) - 1;
                        int day = Integer.parseInt(parts[0]);
                        c.set(year, month, day);
                        return c;
                    } else if (parts.length == 2) {
                        int year = Integer.parseInt(parts[1]);
                        if (parts[1].length() == 2)
                            year = year + 2000; // will this lib life for 100 years ???
                        int month = Integer.parseInt(parts[0]) - 1;
                        c.set(year, month, 1);
                        return c;
                    } else return null;
                }
            }

            try {
                // timestamp
                long ts = Long.valueOf(date);
                c.setTimeInMillis(ts);
                return c;
            } catch (NumberFormatException e) {
            }

            return null;

        } catch (Throwable e) {
            log.t(in, e);
        }

        // return unknown - timestamp is 0
        return null;
    }

    public static void parseTime(Calendar c, String time) {
        time = time.trim();
        String apm = null;
        String zone = null;
        int millies = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;

        // zone
        char sep2 = '?';
        if (MString.isIndex(time, ' ')) sep2 = ' ';
        else if (MString.isIndex(time, '_')) sep2 = '_';
        else if (MString.isIndex(time, 'Z')) sep2 = 'Z';

        if (sep2 == ' ') {
            zone = MString.afterIndex(time, sep2).trim();
            time = MString.beforeIndex(time, sep2).trim();
            if (zone.startsWith("am") || zone.startsWith("pm")) {
                apm = zone.substring(0, 2);
                zone = zone.substring(2).trim();
            }
        } else if (sep2 != '?') {
            zone = MString.afterIndex(time, sep2).trim();
            time = MString.beforeIndex(time, sep2).trim();
        } else if (time.startsWith("+") || time.startsWith("-")) {
            zone = time.trim();
            time = "";
        }

        // milliseconds
        if (MString.isIndex(time, '.')) {
            millies = toint(MString.afterLastIndex(time, '.'), 0);
            time = MString.beforeLastIndex(time, '.');
        }

        time = time.trim();
        if (time.length() > 0) {
            String sep3 = null;
            if (MString.isIndex(time, ':')) sep3 = "\\:";
            else if (MString.isIndex(time, '-')) sep3 = "-";
            else if (MString.isIndex(time, '.')) sep3 = "\\.";

            // parse time
            String[] parts = time.split(sep3);
            if (parts.length > 1) {
                hour = toint(parts[0], 0);
                min = toint(parts[1], 0);
                if (parts.length > 2) sec = toint(parts[2], 0);
            }

            if (apm != null) {
                // https://www.timeanddate.com/time/am-and-pm.html
                if (hour == 0) { // 0 is not valid - reset time to 0
                    min = 0;
                    sec = 0;
                    millies = 0;
                    zone = "";
                } else if (apm.equals("am")) {
                    if (hour == 12) hour = 0;
                } else if (apm.equals("pm")) {
                    if (hour != 12) hour = hour + 12;
                }
            }
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, min);
            c.set(Calendar.MILLISECOND, sec * 1000 + millies);

            if (zone != null && zone.length() > 0) {
                // https://www.timeanddate.com/time/gmt-utc-time.html
                if (zone.startsWith("-") || zone.startsWith("+")) zone = "GMT" + zone;
                TimeZone tz = TimeZone.getTimeZone(zone);
                c.setTimeZone(tz);
            }
        }
    }

    /**
     * Return the value of the month 0 = Januar
     *
     * @param in name or number of the month 1 or 'jan' or 'jnuary' is 0
     * @return string as integer 0=jan, 11=dec or -1 if not found
     */
    public static int toMonth(String in) {
        try {
            int out = Integer.parseInt(in);
            if (out > 0 && out < 13) return out - 1;
        } catch (Throwable t) {
        }
        in = in.toLowerCase().trim();
        Integer nr = monthCatalog.get(in);
        if (nr == null) return -1;
        return nr;
    }

    private static int toint(String in, int def) {
        if (in == null) return def;
        try {
            return Integer.parseInt(in);
        } catch (NumberFormatException nfe) {
            return def;
        }
    }
}
