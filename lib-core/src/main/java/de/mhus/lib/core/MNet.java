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
package de.mhus.lib.core;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class MNet {

    public static boolean isIPv4NetMatch(String network, String ipAddr) {

        String[] parts = network.split("/");
        String ip = parts[0];
        int prefix;

        if (parts.length < 2) {
            prefix = 0;
        } else {
            prefix = Integer.parseInt(parts[1]);
        }

        Inet4Address a = null;
        Inet4Address a1 = null;
        try {
            a = (Inet4Address) InetAddress.getByName(ip);
            a1 = (Inet4Address) InetAddress.getByName(ipAddr);
        } catch (UnknownHostException e) {
            return false;
        }

        byte[] b = a.getAddress();
        int ipInt =
                ((b[0] & 0xFF) << 24)
                        | ((b[1] & 0xFF) << 16)
                        | ((b[2] & 0xFF) << 8)
                        | ((b[3] & 0xFF) << 0);

        byte[] b1 = a1.getAddress();
        int ipInt1 =
                ((b1[0] & 0xFF) << 24)
                        | ((b1[1] & 0xFF) << 16)
                        | ((b1[2] & 0xFF) << 8)
                        | ((b1[3] & 0xFF) << 0);

        int mask = ~((1 << (32 - prefix)) - 1);

        if ((ipInt & mask) == (ipInt1 & mask)) {
            return true;
        } else {
            return false;
        }
    }
    
    // http://svn.apache.org/viewvc/camel/trunk/components/camel-test/src/main/java/org/apache/camel/test/AvailablePortFinder.java?view=markup#l130
    public static final int MIN_PORT_NUMBER = 1100;
    public static final int MAX_PORT_NUMBER = 49151;
    public static boolean availablePort(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
    
}
