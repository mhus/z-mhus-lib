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
package de.mhus.lib.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.logging.Log;

// https://developer.gemalto.com/faq/how-obtain-time-internet-java-rfc868
public class AtomicClockUtil {

    private static Log log = Log.getLog(AtomicClockUtil.class);
    private static final long TIMEOUT_RELOAD = MPeriod.MINUTE_IN_MILLISECOUNDS * 30;
    private static long lastUpdate;
    private static long now;

    // Some time RFC868 servers.
    public static final List<String> TIME_SERVERS =
            new LinkedList<String>(
                    MCollection.toList(
                            new String[] {
                                "time-a.timefreq.bldrdoc.gov",
                                "time-a.timefreq.bldrdoc.gov",
                                "time-b.timefreq.bldrdoc.gov",
                                "time-c.timefreq.bldrdoc.gov",
                                "utcnist.colorado.edu",
                                "time-nw.nist.gov",
                                "nist1.nyc.certifiedtime.com",
                                "nist1.dc.certifiedtime.com",
                                "nist1.sjc.certifiedtime.com",
                                "nist1.datum.com",
                                "ntp2.cmc.ec.gc.ca",
                                "ntps1-0.uni-erlangen.de",
                                "ntps1-1.uni-erlangen.de",
                                "ntps1-2.uni-erlangen.de",
                                "ntps1-0.cs.tu-berlin.de",
                                "time.ien.it",
                                "ptbtime1.ptb.de",
                                "ptbtime2.ptb.de"
                            }));
    private static int currentServer = 0;

    public static long getAtomicTime(String timeServerInternet)
            throws UnknownHostException, IOException, TimeoutException {

        long secondsSince1900 = 0;
        long miliSecondsSince1970 = 0;
        long[] buffer = new long[4];
        boolean readedTime = false;
        int i = 0;
        long timeout = 6000; // * 10 ms

        Socket socket = new Socket(timeServerInternet, 37);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        try {
            while (readedTime == false) {
                // Assumption that the data will be received.
                // As mention not exception control implemented
                if (is.available() >= 4) {
                    // Read the 4 bytes (32 bits) from the server
                    for (i = 0; i < 4; i++) buffer[i] = is.read();
                    // Once finish we can exit of the loop.
                    // Maybe a while implementation is more elegant ;)
                    readedTime = true;
                }
                if (!readedTime) {
                    MThread.sleep(10);
                    timeout--;
                    if (timeout <= 0) throw new TimeoutException();
                }
            }
        } finally {
            // Close the sockets and the connection
            is.close();
            os.close();
            socket.close();
        }

        // Calculate the seconds from 1/1/1990

        secondsSince1900 = buffer[0] * 16777216 + buffer[1] * 65536 + buffer[2] * 256 + buffer[3];

        // Calculate the seconds since 1/1/1970
        // Convert the value to miliseconds.
        // Reason the Java Constructor of the
        // Date Class needs the value in miliseconds and since 1/1/1970
        // that's 2208988800 seconds

        miliSecondsSince1970 = secondsSince1900 - Long.parseLong("2208988800");

        miliSecondsSince1970 = miliSecondsSince1970 * 1000;

        return miliSecondsSince1970;
    }

    /**
     * Returns the current time cached 30 minutes. The time will be interpolated with the current
     * time
     *
     * @return actual time millies in UTC
     */
    public static synchronized long getCurrentTime() {
        if (MPeriod.isTimeOut(lastUpdate, TIMEOUT_RELOAD)) {
            for (int i = 0; i < TIME_SERVERS.size(); i++) {
                try {
                    now = getAtomicTime(TIME_SERVERS.get(currentServer));
                    lastUpdate = System.currentTimeMillis();
                    return now;
                } catch (Throwable t) {
                    log.i(t);
                }
                currentServer = (currentServer + 1) % TIME_SERVERS.size();
            }
        }
        return now + (System.currentTimeMillis() - lastUpdate); // interpolate with last update
    }

    public static void main(String args[]) {
        for (int i = 0; i < 100; i++) {
            long atomic = getCurrentTime();
            long local = System.currentTimeMillis();
            System.out.println("Atomic: " + atomic);
            System.out.println("Local : " + local + "     " + (atomic - local));
            System.out.flush();
            MThread.sleep(100);
        }
    }
}
