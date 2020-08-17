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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * An iterator that 'flattens out' collections, iterators, arrays, etc.
 *
 * <p>That is it will iterate out their contents in order, descending into any iterators, iterables
 * or arrays provided to it.
 *
 * <p>An example (not valid Java for brevity - some type declarations are ommitted):
 *
 * <p>new FlattingIterator({1, 2, 3}, {{1, 2}, {3}}, new ArrayList({1, 2, 3}))
 *
 * <p>Will iterate through the sequence 1, 2, 3, 1, 2, 3, 1, 2, 3.
 *
 * <p>Note that this implements a non-generic version of the Iterator interface so may be cast
 * appropriately - it's very hard to give this class an appropriate generic type.
 *
 * @author david
 * @param <E>
 */
public class FlatteningIterator<E> implements Iterator<E> {
    // Marker object. This is never exposed outside this class, so can be
    // guaranteed
    // to be != anything else. We use it to indicate an absense of any other
    // object.
    private final Object blank = new Object();

    /*
     * This stack stores all the iterators found so far. The head of the stack
     * is the iterator which we are currently progressing through
     */
    private final Stack<Iterator<?>> iterators = new Stack<Iterator<?>>();

    // Storage field for the next element to be returned. blank when the next
    // element
    // is currently unknown.
    private Object next = blank;

    public FlatteningIterator(Object... objects) {
        this.iterators.push(Arrays.asList(objects).iterator());
    }

    @Override
    public void remove() {
        /* Not implemented */ }

    private void moveToNext() {
        if ((next == blank) && !this.iterators.empty()) {
            if (!iterators.peek().hasNext()) {
                iterators.pop();
                moveToNext();
            } else {
                final Object next = iterators.peek().next();
                if (next instanceof Iterator) {
                    iterators.push((Iterator<?>) next);
                    moveToNext();
                } else if (next instanceof Iterable) {
                    iterators.push(((Iterable<?>) next).iterator());
                    moveToNext();
                } else if (next instanceof Array) {
                    iterators.push(Arrays.asList((Array) next).iterator());
                    moveToNext();
                } else this.next = next;
            }
        }
    }

    /**
     * Returns the next element in our iteration, throwing a NoSuchElementException if none is
     * found.
     */
    @Override
    @SuppressWarnings("unchecked")
    public E next() {
        moveToNext();

        if (this.next == blank) throw new NoSuchElementException();
        else {
            Object next = this.next;
            this.next = blank;
            return (E) next;
        }
    }

    /**
     * Returns if there are any objects left to iterate over. This method can change the internal
     * state of the object when it is called, but repeated calls to it will not have any additional
     * side effects.
     */
    @Override
    public boolean hasNext() {
        moveToNext();
        return (this.next != blank);
    }
}
