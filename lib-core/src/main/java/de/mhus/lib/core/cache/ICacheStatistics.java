package de.mhus.lib.core.cache;

import javax.cache.management.CacheStatisticsMXBean;

public interface ICacheStatistics extends CacheStatisticsMXBean {

    long getCacheSize();
    
    long getOccupiedByteSize();
    
}
