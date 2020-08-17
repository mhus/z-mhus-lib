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

import java.util.Collection;
import java.util.Iterator;

public class ImmutableCollection<E> implements Collection<E> {

    private Collection<E> parent;

    public ImmutableCollection(Collection<E> parent) {
        this.parent = parent;
    }

    @Override
    public boolean add(E o) {
        return false;
    }

    public void add(int index, E element) {}

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public void clear() {}

    @Override
    public boolean contains(Object o) {
        return parent.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return parent.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return new ImmutableIterator<E>(parent.iterator());
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    public E remove(int index) {
        return null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public E set(int index, E element) {
        return null;
    }

    @Override
    public int size() {
        return parent.size();
    }

    @Override
    public Object[] toArray() {
        return parent.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return parent.toArray(a);
    }
}
