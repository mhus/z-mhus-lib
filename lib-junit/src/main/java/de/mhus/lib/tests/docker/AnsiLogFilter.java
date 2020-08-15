package de.mhus.lib.tests.docker;

import java.util.LinkedList;

import de.mhus.lib.core.logging.MLogUtil;

public class AnsiLogFilter implements LogFilter {

    @Override
    public void doFilter(LinkedList<Byte> array) {
        try {
            for (int i = 0; i < array.size(); i++) {
                Byte c1 = array.get(i);
                if (c1 != null
                        && (i < 1 || array.get(i - 1) == null || array.get(i - 1) != '\\')
                        && c1 == 27) {
                    array.remove(i); // ESC
                    Byte c2 = array.remove(i); // first letter
                    if (c2 != null && c2 == '[') {
                        while (array.size() > i) {
                            Byte c3 = array.remove(i);
                            if (c3 != null && (c3 >= 'a' && c3 <= 'z' || c3 >= 'A' && c3 <= 'Z')) {
                                if (i > 0) i--;
                                break;
                            }
                        }
                    } else if (i > 0) i--;
                }
            }
        } catch (NullPointerException npe) {
            MLogUtil.log().t(npe);
        }
    }
}
