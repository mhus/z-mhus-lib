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
package de.mhus.lib.core.util;

import java.util.Iterator;
import java.util.ListIterator;

import de.mhus.lib.basics.ReadOnly;

public class SingleIterator<E> implements Iterator<E>, ListIterator<E>, ReadOnly {

    private E element = null;

    public SingleIterator(E element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return (element != null);
    }

    @Override
    public E next() {
        E next = element;
        element = null;
        return next;
    }

    @Override
    public void remove() {}

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public E previous() {
        return null;
    }

    @Override
    public int nextIndex() {
        return hasNext() ? 0 : -1;
    }

    @Override
    public int previousIndex() {
        return -1;
    }

    @Override
    public void set(E e) {}

    @Override
    public void add(E e) {}
}
