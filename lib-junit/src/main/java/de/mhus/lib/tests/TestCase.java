/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.tests;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MStopWatch;

public class TestCase {

    private MStopWatch timer;
    protected boolean skipTest = false;
    private static PrintStream out;
    private static PrintStream err;
    private static InputStream in;
    protected static boolean waitBeforeExecute = false;

    @BeforeAll
    public static void beforeAll(TestInfo testInfo) {
        System.out.println();
        Optional<Class<?>> clazz = testInfo.getTestClass();
        if (clazz.isPresent())
            System.out.println("START TEST FOR " + clazz.get().getCanonicalName());
        else
            System.out.println("START TEST FOR UNKNOWN");
        out = System.out;
        err = System.err;
        in = System.in;
        if (MCast.toboolean(System.getenv().getOrDefault("test.waitBeforeExecute", ""), false))
            waitBeforeExecute = true;
    }
    @BeforeEach
    public void beforeEach(TestInfo testInfo) throws SkipTestException {
        
        if (out != null) System.setOut(out);
        if (err != null) System.setErr(err);
        if (in != null) System.setIn(in);
        
        if (skipTest)
            throw new SkipTestException();
        
        System.out.println();
        System.out.println("--------------------------------------------------");
        TestUtil.start(testInfo);
        System.out.println("--------------------------------------------------");
        
        if (waitBeforeExecute) {
            System.out.println("Press ENTER to start test");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            scanner.close();
        }
        timer = new MStopWatch().start();
    }

    @AfterEach
    public void afterEach(TestInfo testInfo) {
        timer.stop();
        
        if (out != null) System.setOut(out);
        if (err != null) System.setErr(err);
        if (in != null) System.setIn(in);
        
        System.out.println();
        System.out.println("--------------------------------------------------");
        TestUtil.stop(testInfo);
        System.out.println("Time: " + timer.getCurrentTimeAsString());
        System.out.println("--------------------------------------------------");
    }

}
