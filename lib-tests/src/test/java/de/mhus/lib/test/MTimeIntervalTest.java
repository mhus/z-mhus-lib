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

import java.util.Date;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.tests.TestCase;

public class MTimeIntervalTest extends TestCase {

    @Test
    public void testAverageMonth() {

        int period = 10000; // years

        Date from = MCast.toDate("2000-01-01T00:00:00", null);
        Date to = MDate.toDate((2000 + period) + "-01-01T00:00:00", null);

        long diff = to.getTime() - from.getTime();

        long average = diff / (period * 12);
        System.out.println("Average month ms for " + period + " years is " + average);
        {
            MPeriod interval = new MPeriod(average);
            System.out.println("Manual  average month days is " + (average / 1000 / 60 / 60 / 24));
            System.out.println("AllDays average month days is " + interval.getAllDays());
            assertEquals(30, interval.getAllDays());
        }
        {
            MPeriod interval = new MPeriod("1y");
            assertEquals(1, interval.getAverageYears());
            assertEquals(12, interval.getAverageMonths());
            System.out.println("1 year days is " + interval.getAllDays());
        }
        {
            // days of 10.000 years
            MPeriod interval = new MPeriod(period + "y");
            float days = (diff / (1000 * 60 * 60 * 24));
            assertEquals(days, interval.getAllDays());
        }
    }

    @Test
    public void testParse() {
        {
            MPeriod i = new MPeriod("1h");
            assertEquals(MPeriod.HOUR_IN_MILLISECOUNDS, i.getAllMilliseconds());
        }
        {
            MPeriod i = new MPeriod("1M");
            assertEquals(MPeriod.MINUTE_IN_MILLISECOUNDS, i.getAllMilliseconds());
        }
        {
            MPeriod i = new MPeriod("1d");
            assertEquals(MPeriod.DAY_IN_MILLISECOUNDS, i.getAllMilliseconds());
        }
        {
            MPeriod i = new MPeriod("1d 1h");
            assertEquals(1, i.getAllDays());
        }
        {
            MPeriod i = new MPeriod("1d 24h");
            assertEquals(2, i.getAllDays());
        }
        {
            MPeriod i = new MPeriod("1d 25h");
            assertEquals(2, i.getAllDays());
        }
    }

    @Test
    public void testGet() {

        MPeriod i = new MPeriod("1d 1h 5M");
        assertEquals(1, i.getAllDays());

        assertEquals(25, i.getAllHours());

        assertEquals(25 * 60 + 5, i.getAllMinutes());
    }

    {
        MPeriod i = new MPeriod(2629746000l);
        assertEquals(2629746000l, i.getAllMilliseconds());
        assertEquals(2629746, i.getAllSecounds());
        assertEquals(2629746 / 60, i.getAllMinutes());
        assertEquals(2629746 / 60 / 60, i.getAllHours());
        assertEquals(2629746 / 60 / 60 / 24, i.getAllDays());
    }
}
