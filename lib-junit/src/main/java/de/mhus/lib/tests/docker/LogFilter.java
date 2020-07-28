package de.mhus.lib.tests.docker;

import java.util.LinkedList;

public interface LogFilter {

    void doFilter(LinkedList<Byte> array);

}
