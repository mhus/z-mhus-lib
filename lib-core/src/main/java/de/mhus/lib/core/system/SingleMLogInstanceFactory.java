/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.system;

import java.util.WeakHashMap;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogFactory;

@JmxManaged
public class SingleMLogInstanceFactory extends MJmx implements MLogFactory {

    private WeakHashMap<String, Log> cache = new WeakHashMap<>();

    @SuppressWarnings("rawtypes")
    @Override
    public Log lookup(Object owner) {
        String name = null;
        if (owner == null) name = null;
        else if (owner instanceof String) name = (String) owner;
        else if (owner instanceof Class) name = ((Class) owner).getCanonicalName();
        else name = MSystem.getClassName(owner);
        synchronized (this) {
            Log log = cache.get(name);
            if (log == null) log = new Log(name);
            cache.put(name, log);
            return log;
        }
    }

    @JmxManaged
    public int getCacheSize() {
        return cache.size();
    }

    @Override
    public void update() {
        synchronized (this) {
            for (Log l : cache.values()) l.update();
        }
    }
}
