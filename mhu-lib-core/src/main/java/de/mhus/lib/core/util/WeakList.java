/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class WeakList<E> extends AbstractList<E>{
	
	private LinkedList<WeakReference<E>> items = new LinkedList<>();
	
	public WeakList() {
    }
    
    public WeakList(Collection<E> c) {
        addAll(0, c);
    }
    
    @Override
	public void add(int index, E element) {
        items.add(index, new WeakReference<>(element));
    }
    
	@Override
	public E get(int index) {
		WeakReference<E> weak = items.get(index);
		return weak.get();
	}

	@Override
	public int size() {
		cleanupWeak();
		return items.size();
	}

	public void cleanupWeak() {
	    try { 
	        // java bug? 
	        // Caused by: java.lang.NullPointerException
	        // at java.util.LinkedList$ListItr.next(LinkedList.java:897) ~[?:?]
	        // at java.util.Collection.removeIf(Collection.java:544) ~[?:?]
	        items.removeIf(i -> i.get() == null);
	    } catch (Throwable t) {
	    }
	}

	@Override
	public Iterator<E> iterator() {
		cleanupWeak();
        return toList().iterator();
    }

	private AbstractList<E> toList() {
		final LinkedList<E> out = new LinkedList<>();
		items.forEach(i -> {E v = i.get(); if (v != null) out.add(v);});
		return out;
	}
	
}
