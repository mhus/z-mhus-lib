package de.mhus.lib.core.util;

import java.util.Iterator;

public class RangeIterator implements Iterator<Integer> {

    private int next;
    private int to;
    private int step;

    public RangeIterator(int from, int to) {
        this(from, to, from < to ? -1 : 1);
    }
    
    public RangeIterator(int from, int to, int step) {
        this.next = from;
        this.to = to;
        this.step = step;
        if (step == 0) throw new NullPointerException("step can't be 0"); // 0 is also a null :)
    }
    
    @Override
    public boolean hasNext() {
        return step > 0 ? (next < to) : (next > to);
    }

    @Override
    public Integer next() {
        int c = next;
        next = next + step;
        return c;
    }

}
