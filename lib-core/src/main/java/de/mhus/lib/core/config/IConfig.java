package de.mhus.lib.core.config;

import java.util.List;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.NotFoundException;

/**
 * A IConfig extends the concept of properties to a object oriented structure.
 * A property can also be an object or array of objects. The IConfig will not
 * really separate objects and arrays. If you require an array and it's only a 
 * single objects you will get a list with a single object and vies versa.
 * 
 * @author mikehummel
 *
 */
public interface IConfig extends IProperties {

    public static final String NAMELESS_VALUE = "";

    /**
     * Returns true if the key is an object.
     * 
     * @param key
     * @return If the property is an object or array
     */
    boolean isObject(String key);

    IConfig getObjectOrNull(String key);
    
    IConfig getObject(String key) throws NotFoundException;

    boolean isArray(String key);

    ConfigList getArray(String key) throws NotFoundException;

    IConfig getObjectByPath(String path);

    String getExtracted(String key, String def);

    String getExtracted(String key);

    List<IConfig> getObjects();

    void setObject(String key, IConfig object);

    void addObject(String key, IConfig object);

    IConfig createObject(String key);

    List<String> getPropertyKeys();

    String getName();

    IConfig getParent();

    List<String> getObjectKeys();

    /**
     * Return a iterator over a array or a single object.
     * Return an empty iterator if not found.
     * Use this function to iterate over arrays or objects.
     * 
     * @param key
     * @return Never null.
     */
    List<IConfig> getObjectList(String key);

    List<String> getObjectAndArrayKeys();

    List<String> getArrayKeys();

    ConfigList getArrayOrNull(String key);

    ConfigList getArrayOrCreate(String key);

    ConfigList createArray(String key);

//    IConfig cloneObject(IConfig node);
    
}
