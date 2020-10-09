/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.cfg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

public class MCfgUpdater {

    @SuppressWarnings("rawtypes")
    //	private WeakHashMap<CfgValue, String> registry = new WeakHashMap<>();
    private HashMap<String, HashMap<String, WeakHashMap<CfgValue, String>>> registry =
            new HashMap<>();

    @SuppressWarnings("rawtypes")
    protected synchronized WeakHashMap<CfgValue, String> getCfgContainer(
            String owner, String path) {
        if (path == null) path = "";
        HashMap<String, WeakHashMap<CfgValue, String>> ownerContainer = registry.get(owner);
        if (ownerContainer == null) {
            ownerContainer = new HashMap<>();
            registry.put(owner, ownerContainer);
        }
        WeakHashMap<CfgValue, String> pathContainer = ownerContainer.get(path);
        if (pathContainer == null) {
            pathContainer = new WeakHashMap<>();
            ownerContainer.put(path, pathContainer);
        }
        return pathContainer;
    }

    @SuppressWarnings("rawtypes")
    protected synchronized HashMap<String, WeakHashMap<CfgValue, String>> getOwnerContainers(
            String owner) {
        HashMap<String, WeakHashMap<CfgValue, String>> ownerContainer = registry.get(owner);
        if (ownerContainer == null) {
            ownerContainer = new HashMap<>();
            registry.put(owner, ownerContainer);
        }
        return ownerContainer;
    }

    @SuppressWarnings("rawtypes")
    public void register(CfgValue configValue) {
        synchronized (registry) {
            getCfgContainer(configValue.getOwner(), configValue.getPath()).put(configValue, "");
        }
    }

    @SuppressWarnings("rawtypes")
    public void doUpdate(String owner) {
        LinkedList<CfgValue> list = new LinkedList<>();
        synchronized (registry) {
            if (owner == null) {
                for (HashMap<String, WeakHashMap<CfgValue, String>> cfgs : registry.values())
                    for (WeakHashMap<CfgValue, String> cfg : cfgs.values())
                        list.addAll(cfg.keySet());
            } else
                for (WeakHashMap<CfgValue, String> pathContainer :
                        getOwnerContainers(owner).values()) list.addAll(pathContainer.keySet());
        }
        for (CfgValue<?> item : list)
            if (owner == null
                    || item.isOwner(owner)) // is not working at all, owner could be a super class
            item.update();
    }

    @SuppressWarnings("rawtypes")
    public void doUpdate(String owner, String path) {
        LinkedList<CfgValue> list = null;
        synchronized (registry) {
            list = new LinkedList<CfgValue>(getCfgContainer(owner, path).keySet());
        }
        for (CfgValue<?> item : list)
            if ((owner == null || item.isOwner(owner))
                    && path.equals(
                            item.getPath())) // is not working at all, owner could be a super class
            item.update();
    }

    @SuppressWarnings("rawtypes")
    public List<CfgValue> getList() {
        LinkedList<CfgValue> list = new LinkedList<>();
        synchronized (registry) {
            for (HashMap<String, WeakHashMap<CfgValue, String>> ownerContainer :
                    registry.values()) {
                for (WeakHashMap<CfgValue, String> pathContainer : ownerContainer.values()) {
                    list.addAll(pathContainer.keySet());
                }
            }
        }
        return list;
    }
}
