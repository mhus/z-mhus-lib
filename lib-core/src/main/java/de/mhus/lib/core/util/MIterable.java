package de.mhus.lib.core.util;

import java.util.Iterator;

public class MIterable<T> implements Iterable<T> {

    private Iterator<T> iterator;

    public MIterable(Iterator<T> iterator) {
        this.iterator = iterator;
    }
    
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

}
