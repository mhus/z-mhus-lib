package de.mhus.lib.core.cache;

import javax.cache.management.CacheStatisticsMXBean;

public interface LocalCacheStatistics extends CacheStatisticsMXBean {

    long getCacheSize();
    
    long getOccupiedByteSize();
    
}
