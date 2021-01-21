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

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.schedule.CronJob;
import de.mhus.lib.core.schedule.CronJob.Definition;
import de.mhus.lib.tests.TestCase;

public class SchedulerTest extends TestCase {

    @Test
    public void testCron() {
        {
            Definition def = new CronJob.Definition("* * * * *");
            Date start = MDate.toDate("19-07-07 10:10:10", null);
            Date next = new Date(def.calculateNext(start.getTime()));
            assertEquals(MDate.toDate("19-07-07 10:11:00", null), next);
        }
        {
            Definition def = new CronJob.Definition("15,30,45,0 * * * *");
            Date start = MDate.toDate("19-07-07 10:10:10", null);
            Date next = new Date(def.calculateNext(start.getTime()));
            assertEquals(MDate.toDate("19-07-07 10:15:00", null), next);
        }
        {
            Definition def = new CronJob.Definition("15-30 * * * *");
            Date start = MDate.toDate("19-07-07 10:10:10", null);
            Date next = new Date(def.calculateNext(start.getTime()));
            assertEquals(MDate.toDate("19-07-07 10:15:00", null), next);
        }
        {
            Definition def = new CronJob.Definition("15-30/5 * * * *");
            Date start = MDate.toDate("19-07-07 10:16:10", null);
            Date next = new Date(def.calculateNext(start.getTime()));
            assertEquals(MDate.toDate("19-07-07 10:20:00", null), next);
        }
        {
            Definition def = new CronJob.Definition("* * * * 2-6");
            Date start = MDate.toDate("2018-02-24 10:10:10", null);
            Date next = new Date(def.calculateNext(start.getTime()));
            assertEquals(MDate.toDate("2018-02-26 0:00:00", null), next);
        }
        {
            Definition def = new CronJob.Definition("* * * * Mo-fr");
            Date start = MDate.toDate("2018-02-24 10:10:10", null);
            Date next = new Date(def.calculateNext(start.getTime()));
            assertEquals(MDate.toDate("2018-02-26 0:00:00", null), next);
        }
        {
            Definition def = new CronJob.Definition("* * * * * w");
            Date start = MDate.toDate("2018-01-01 10:10:10", null);
            Date next = new Date(def.calculateNext(start.getTime()));
            assertEquals(MDate.toDate("2018-01-02 0:00:00", null), next);
        }
    }
}
