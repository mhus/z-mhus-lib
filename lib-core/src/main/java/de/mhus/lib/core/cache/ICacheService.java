/**
 * Copyright (C) 2018 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.cache;

import java.io.Serializable;
import java.util.List;

public interface ICacheService {

    /**
     * Create or get a cache handler also for in memory usage
     * 
     * @param <K>
     * @param <V>
     * @param owner
     * @param name
     * @param keyType
     * @param valueType
     * @param config
     * @return New or existing cache handler
     */
    <K, V> ICache<K, V> createCache(
            Object owner,
            String name,
            Class<K> keyType,
            Class<V> valueType,
            CacheConfig config
            );

    /**
     * Create a cache handler to be stored or shared.
     * 
     * @param <K>
     * @param <V>
     * @param owner
     * @param name
     * @param keyType
     * @param valueType
     * @param config
     * @return New or existing cache handler
     */
    default <K extends Serializable, V extends Serializable> ICache<K, V> createSerilizableCache(
            Object owner,
            String name,
            Class<K> keyType,
            Class<V> valueType,
            CacheConfig config
            ) {
        if (config == null) config = new CacheConfig();
        config.setSerializable(true);
        return createCache(owner, name, keyType, valueType, config);
    }

    List<String> getCacheNames();

    <K, V> ICache<K, V> getCache(String name);
}
