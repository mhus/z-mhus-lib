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

import java.util.List;

public interface LocalCacheService {

    // ccb -> ccb.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(5)))

//    CacheManagerBuilder<CacheManager> getCacheBuilder();

    <K, V> LocalCache<K, V> createCache(
            Object owner,
            String name,
            Class<K> keyType,
            Class<V> valueType,
            LocalCacheConfig config
            );

    List<String> getCacheNames();

    <K, V> LocalCache<K, V> getCache(String name);
}
