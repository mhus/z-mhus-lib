package de.mhus.lib.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import de.mhus.lib.core.MStopWatch;

class InternatTestCase {

    private MStopWatch timer;

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.println("--------------------------------------------------");
        InternatTestUtil.start(testInfo);
        System.out.println("--------------------------------------------------");
        timer = new MStopWatch().start();
    }
    
    @AfterEach
    public void afterEach() {
        timer.stop();
        System.out.println("--------------------------------------------------");
        System.out.println("Time: " + timer.getCurrentTimeAsString());
        System.out.println("--------------------------------------------------");
        
    }
}
