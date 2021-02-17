package de.mhus.lib.core.config;

public interface ConfigSerializable {

    
    /**
     * Read the inner state of the object from the given config object.
     * @param cfg Config storing the state
     * @throws Exception 
     */
    void readSerializableConfig(IConfig cfg) throws Exception;
    
    /**
     * Write the inner state of the object to the given config object.
     * @param cfg Config to store the state
     * @throws Exception 
     */
    void writeSerializableConfig(IConfig cfg) throws Exception;
    
}
