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
package de.mhus.lib.core.util;

import java.util.Iterator;

public class RangeIterator implements Iterator<Integer> {

    private int next;
    private int to;
    private int step;

    public RangeIterator(int from, int to) {
        this(from, to, from < to ? -1 : 1);
    }

    public RangeIterator(int from, int to, int step) {
        this.next = from;
        this.to = to;
        this.step = step;
        if (step == 0) throw new NullPointerException("step can't be 0"); // 0 is also a null :)
    }

    @Override
    public boolean hasNext() {
        return step > 0 ? (next < to) : (next > to);
    }

    @Override
    public Integer next() {
        int c = next;
        next = next + step;
        return c;
    }
}
