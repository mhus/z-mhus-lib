package de.mhus.lib.core.cache;

public class CacheConfig {

    private int heapSize = 0;
    private boolean serializable = false;
    private long ttl = 0;
    
    public int getHeapSize() {
        return heapSize;
    }

    public CacheConfig setHeapSize(int heapSize) {
        this.heapSize = heapSize;
        return this;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public CacheConfig setSerializable(boolean serializable) {
        this.serializable = serializable;
        return this;
    }

    public long getTTL() {
        return ttl;
    }

    public CacheConfig setTTL(long ttl) {
        this.ttl = ttl;
        return this;
    }
    
}
