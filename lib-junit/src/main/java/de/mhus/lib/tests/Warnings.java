package de.mhus.lib.tests;

import de.mhus.lib.core.MCast;

public class Warnings {

    public static void warnTrue(boolean condition) {
        if (!condition) {
            System.err.println("+++ WARNING");
            System.err.println(MCast.toString(new Throwable()));
        }
    }

    public static void warnFalse(boolean condition) {
        if (condition) {
            System.err.println("+++ WARNING");
            System.err.println(MCast.toString(new Throwable()));
        }
    }
    
}
