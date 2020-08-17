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

import de.mhus.lib.basics.ReadOnly;

public class ArrayIterator<T> implements Iterator<T>, ReadOnly {

    private T[] from;
    private int pos;
    private int max;

    public ArrayIterator(T[] from, int start, int stop) {
        this.from = from;
        pos = Math.max(start, 0);
        max = Math.min(stop, from.length);
    }

    public ArrayIterator(T[] from) {
        this.from = from;
        pos = 0;
        max = from.length;
    }

    @Override
    public boolean hasNext() {
        return pos < max;
    }

    @Override
    public T next() {
        pos++;
        return from[pos - 1];
    }

    @Override
    public void remove() {}
}
