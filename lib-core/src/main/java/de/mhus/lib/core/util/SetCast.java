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
package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class SetCast<F, T> implements Set<T> {

    private Set<F> from;

    public SetCast(Set<F> from) {
        this.from = from;
    }

    @Override
    public int size() {
        return from.size();
    }

    @Override
    public boolean isEmpty() {
        return from.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return from.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorCast<F, T>(from.iterator());
    }

    @Override
    public Object[] toArray() {
        return from.toArray();
    }

    @SuppressWarnings("hiding")
    @Override
    public <T> T[] toArray(T[] a) {
        return from.toArray(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(T e) {
        return from.add((F) e);
    }

    @Override
    public boolean remove(Object o) {
        return from.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return from.containsAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return from.addAll((Collection<? extends F>) c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return from.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return from.removeAll(c);
    }

    @Override
    public void clear() {
        from.clear();
    }

    @Override
    public String toString() {
        return String.valueOf(from);
    }
}
