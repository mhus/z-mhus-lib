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
/*
VersionCheck: Check that Java version is sufficiently recent

copyright (c) 1997-2006 Roedy Green, Canadian Mind Products
may be copied and used freely for any purpose but military.
Roedy Green
Canadian Mind Products
#101 - 2536 Wark Street
Victoria, BC Canada
V8T 4G8
tel: (250) 361-9093
mailto:roedyg@mindprod.com
http://mindprod.com

version history

version 1.7 2006-03-04 reformat with IntelliJ, provide Javadoc

version 1.5 2005-07-14 split off from Misc, allow for compilation with old compiler.

version 1.4 2002 August 17, add quoteSQL

version 1.3 1999 August 24, add leftPad, rightPad, smarter rep.
 isJavaVersionOK now handles 1.3beta.

version 1.2 1998 December  14, add isJavaVersionOK

version 1.1 1998 November 10, add dates

version 1.0 1997 March 23, initial.

 */
package de.mhus.lib.core.util;

import java.awt.Color;
import java.awt.Container;
import java.awt.TextArea;

/**
 * Check that Java version is sufficiently recent
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.7, 2006-03-04
 */
public class JreVersionCheck {

    // ------------------------------ FIELDS ------------------------------

    /** true if you want extra debugging output and test code */
    static final boolean DEBUGGING = false;

    // -------------------------- STATIC METHODS --------------------------

    /**
     * Ensures Java runtime version e.g. 1.1.7 is sufficiently recent. Based on code by Dr. Tony
     * Dahlman <adahlman@jps.net>
     *
     * @param wantedMajor java major version e.g. 1
     * @param wantedMinor Java minor version e.g. 1
     * @param wantedBugFix Java bugfix version e.g. 7
     * @return true if JVM version running is equal to or more recent than (higher than) the level
     *     specified.
     */
    public static boolean isJavaVersionOK(int wantedMajor, int wantedMinor, int wantedBugFix) {
        try {
            try {
                // java.version will have form 1.1.7A, 11, 1.1., 1.1, 1.3beta,
                // 1.4.2_05 or 1.4.1-rc
                // It may be gibberish. It may be undefined.
                // We have do deal with all this malformed garbage.
                // Because incompetents run the world,
                // it is not nicely formatted for us in three fields.
                String ver = System.getProperty("java.version");

                if (ver == null) {
                    return false;
                }

                ver = ver.trim();

                if (ver.length() < 2) {
                    return false;
                }

                int dex = ver.indexOf('.');

                if (dex < 0) {
                    // provide missing dot
                    ver = ver.charAt(0) + '.' + ver.substring(1);
                    dex = 1;
                }

                int gotMajor = Integer.parseInt(ver.substring(0, dex));
                if (DEBUGGING) {
                    System.out.println("major:" + gotMajor);
                }
                if (gotMajor < wantedMajor) {
                    return false;
                }
                if (gotMajor > wantedMajor) {
                    return true;
                }

                // chop off major and first dot.
                ver = ver.substring(dex + 1);

                // chop trailing "beta"
                if (ver.endsWith("beta")) {
                    ver = ver.substring(0, ver.length() - "beta".length());
                }
                // chop trailing "-rc"
                if (ver.endsWith("-rc")) {
                    ver = ver.substring(0, ver.length() - "-rc".length());
                }
                // chop any trailing _nn
                dex = ver.lastIndexOf('_');
                if (dex >= 0) {
                    ver = ver.substring(0, dex);
                }
                // chop any trailing letter as in 1.1.7A,
                // but convert 1.1.x or 1.1.X to 1.1.9
                char ch = ver.charAt(ver.length() - 1);
                if (!Character.isDigit(ch)) {
                    ver = ver.substring(0, ver.length() - 1);
                    if (ch == 'x' || ch == 'X') {
                        ver += '9';
                    }
                }
                // check minor version
                dex = ver.indexOf('.');
                if (dex < 0) {
                    // provide missing BugFix number as in 1.2 or 1.0
                    ver += ".0";
                    dex = ver.indexOf('.');
                }

                int gotMinor = Integer.parseInt(ver.substring(0, dex));
                if (DEBUGGING) {
                    System.out.println("minor:" + gotMinor);
                }
                if (gotMinor < wantedMinor) {
                    return false;
                }
                if (gotMinor > wantedMinor) {
                    return true;
                }
                // was equal, need to examine third field.
                // check bugfix version
                ver = ver.substring(dex + 1);
                int gotBugFix = Integer.parseInt(ver);
                if (DEBUGGING) {
                    System.out.println("bugFix:" + gotBugFix);
                }
                return (gotBugFix >= wantedBugFix);
            } catch (NumberFormatException e) {
                if (DEBUGGING) {
                    System.out.println("number format" + e.getMessage());
                }
                return false;
            } // end catch
        } catch (StringIndexOutOfBoundsException e) {
            if (DEBUGGING) {
                System.out.println("out of bounds:" + e.getMessage());
            }

            return false;
        } // end catch
    } // end isJavaVersionOK

    /**
     * use in a paint routine if Java version is not ok, usually tested statically.
     *
     * @param wantedMajor java major version e.g. 1
     * @param wantedMinor Java minor version e.g. 1
     * @param wantedBugFix Java bugfix version e.g. 7
     * @param container container to add an error message component.
     * @return true if version is ok
     */
    public static boolean isJavaVersionOK(
            int wantedMajor, int wantedMinor, int wantedBugFix, Container container) {
        if (isJavaVersionOK(wantedMajor, wantedMinor, wantedBugFix)) {
            return true;
        } else {
            String error =
                    "Error: You need Java "
                            + wantedMajor
                            + "."
                            + wantedMinor
                            + "."
                            + wantedBugFix
                            + " or later to run this Applet.\n"
                            + "You are currently running under Java "
                            + System.getProperty("java.version")
                            + ".\n"
                            + "Get the latest Java from http://java.com/en/index.jsp";
            TextArea complain = new TextArea(error, 3, 42, TextArea.SCROLLBARS_NONE);

            complain.setEditable(false);
            complain.setBackground(Color.white);
            complain.setForeground(Color.red);
            complain.setSize(300, 50);
            container.setLayout(null);
            container.add(complain);
            System.err.println(error);
            return false;
        }
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    /** VersionCheck contains only static methods. */
    private JreVersionCheck() {}
} // end class VersionCheck
