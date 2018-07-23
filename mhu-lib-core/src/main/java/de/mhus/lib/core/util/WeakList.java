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
		items.removeIf(i -> i.get() == null);
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
