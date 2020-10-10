package de.mhus.lib.core.shiro;

public class BearerConfiguration {

    private long timeout = 0;

    public BearerConfiguration() {}
    
    public BearerConfiguration(long timeout) {
        this.timeout = timeout;
    }
    
    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public boolean isTimeout() {
        return timeout > 0;
    }
    
    public long getTTL() {
        if (timeout <= 0) return 0;
        return System.currentTimeMillis() + timeout;
    }
    
}
