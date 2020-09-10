package de.mhus.lib.core.util;

import java.util.Iterator;

/**
 * Map an existing iterator into Iterable
 * @author mikehummel
 * @param <T> 
 *
 */
public class Iterate<T> implements Iterable<T>{

    private Iterator<T> iterator;

    public Iterate(Iterator<T> iterator) {
        this.iterator = iterator;
    }
    
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

}
