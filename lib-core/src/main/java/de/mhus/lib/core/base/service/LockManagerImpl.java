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
package de.mhus.lib.core.base.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.concurrent.LocalLock;
import de.mhus.lib.core.concurrent.Lock;
import de.mhus.lib.core.util.SoftHashMap;
import de.mhus.lib.core.util.WeakList;

/**
 * The manager have got two mechanisms. First will create locks like a factory. Second will register
 * created new Lock() locks.
 *
 * @author mikehummel
 */
public class LockManagerImpl extends MLog implements LockManager {

    private HashMap<String, Lock> locks = new HashMap<>();
    private SoftHashMap<String, Lock> cache = new SoftHashMap<>();
    private WeakList<Lock> register = new WeakList<>();

    public LockManagerImpl() {
        log().i("Start DefaultLockManager");
    }

    @Override
    public Lock getLock(String name) {
        Lock lock = null;
        synchronized (cache) {
            lock = cache.get(name);
            if (lock == null) {
                lock = new ManagedLock(name);
                cache.put(name, lock);
            }
        }
        return lock;
    }

    @Override
    public Lock getLock(String name, Function<String, Lock> creator) {
        Lock lock = null;
        synchronized (cache) {
            lock = cache.get(name);
            if (lock == null) {
                lock = creator.apply(name);
                if (lock != null) cache.put(name, lock);
            }
        }
        return lock;
    }

    class ManagedLock extends LocalLock {
        public ManagedLock(String name) {
            super(name);
        }

        @Override
        protected void register() {}
    }

    class ManagerLock extends LocalLock {
        @Override
        protected void lockEvent(boolean locked) {
            synchronized (cache) {
                if (locked) {
                    locks.put(name, this);
                    if (!cache.containsKey(name)) // could be removed in the meantime ?
                    cache.put(name, this);
                } else {
                    locks.remove(name);
                }
            }
        }
    }

    @Override
    public Lock[] managedLocks() {
        synchronized (cache) {
            LinkedList<Lock> list = new LinkedList<>();
            for (Lock item : cache.values()) if (item != null) list.add(item);
            return list.toArray(new Lock[list.size()]);
        }
    }

    @Override
    public void register(Lock lock) {
        register.add(lock);
    }

    @Override
    public Lock[] getRegisteredLocks() {
        return register.toArray(new Lock[register.size()]);
    }
}
