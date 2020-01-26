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

import java.util.Iterator;

import de.mhus.lib.core.logging.MLogUtil;

public abstract class ObjectStream<T> implements Iterable<T> {

    private T next = null;

    private Iterator<T> iterator =
            new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return ObjectStream.this.hasNext();
                }

                @Override
                public T next() {
                    if (next == null) return null;
                    T result = next;
                    findNext();
                    return result;
                }
            };

    public ObjectStream() {
        findNext();
    }

    private void findNext() {
        try {
            next = getNext();
        } catch (Throwable t) {
            MLogUtil.log().d(t);
            next = null;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    public boolean hasNext() {
        return next != null;
    }

    /**
     * Return the next object or null if the end of the stream is reached.
     *
     * @return
     */
    protected abstract T getNext();
}
