package de.mhus.lib.basics;

import java.io.Closeable;

public interface MCloseable extends Closeable {

    @Override
    public void close();

}
