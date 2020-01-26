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
package de.mhus.lib.core.console;

/**
 * @author hummel
 *     <p>To change the template for this generated type comment go to
 *     Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsoleProgressBar {

    int len = 0;
    long max = 0;
    long current = 0;

    long start = -1;
    long stop = -1;

    Console stream;

    public ConsoleProgressBar(Console console) {
        this(console, 0, 100);
    }

    public ConsoleProgressBar(Console console, long _max) {
        this(console, 0, _max);
    }

    public ConsoleProgressBar(Console console, int _len, long _max) {
        this.stream = console;
        if (_len < 1) _len = console.getWidth();
        len = _len;
        max = _max;
    }

    public void add(long _add) {
        set(current + _add);
    }

    public void set(long _current) {

        if (stop != -1) return;
        if (start == -1) {
            start = System.currentTimeMillis();
            paintHeader();
        }

        if (_current > max) _current = max;
        if (_current < 0) _current = 0;
        if (_current < current) clean();
        paint(_current);
    }

    public void clean() {

        if (stop != -1) return;

        current = 0;
        stream.cr();
        for (int i = 0; i < len; i++) stream.print(' ');
        stream.cr();
    }

    private void paintHeader() {

        for (int i = 0; i < len; i++) {
            if (i == 0 || i == len - 1) {
                stream.print('|');
            } else if (i % 10 == 0) {
                stream.print('+');
            } else stream.print('-');
        }
        stream.println();
    }

    private void paint(long _current) {
        int old = (int) ((double) len / (double) max * (double) current);
        int new_ = (int) ((double) len / (double) max * (double) _current);
        int diff = new_ - old;
        for (int i = 0; i < diff; i++) stream.print('*');
        current = _current;
    }

    public void finish() {
        stream.println();
        stop = System.currentTimeMillis();
    }

    public long getTime() {
        if (stop == -1) return (System.currentTimeMillis() - start) / 1000;
        else return (stop - start) / 1000;
    }

    /** @return Rate per milliseconds */
    public float getRate() {
        long time = getTime();
        if (time == 0) return (float) current;
        return (float) current / (float) time;
    }
}
