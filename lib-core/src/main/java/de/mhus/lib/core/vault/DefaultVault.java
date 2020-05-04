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
package de.mhus.lib.core.vault;

import java.util.HashMap;
import java.util.UUID;

import de.mhus.lib.core.util.MObject;

public class DefaultVault extends MObject implements MVault {

    private HashMap<String, VaultSource> sources = new HashMap<>();
    private MVault parent;

    public DefaultVault() {}

    public DefaultVault(MVault parent) {
        this.parent = parent;
    }

    @Override
    public void registerSource(VaultSource source) {
        if (source == null) return;
        synchronized (sources) {
            sources.put(source.getName(), source);
        }
    }

    @Override
    public void unregisterSource(String sourceName) {
        if (sourceName == null) return;
        synchronized (sources) {
            sources.remove(sourceName);
        }
    }

    @Override
    public String[] getSourceNames() {
        if (parent != null) {
            String[] parentNames = parent.getSourceNames();
            synchronized (sources) {
                String[] out = new String[parentNames.length + sources.size()];
                int cnt = 0;
                for (String name : sources.keySet()) {
                    out[cnt] = name;
                    cnt++;
                }
                for (String name : parentNames) {
                    out[cnt] = name;
                    cnt++;
                }
                return out;
            }
        }
        synchronized (sources) {
            return sources.keySet().toArray(new String[sources.size()]);
        }
    }

    @Override
    public VaultSource getSource(String name) {
        if (name == null) return null;
        synchronized (sources) {
            VaultSource ret = sources.get(name);
            if (ret == null && parent != null) return parent.getSource(name);
            else return ret;
        }
    }

    @Override
    public VaultEntry getEntry(UUID id) {
        if (id == null) return null;
        synchronized (sources) {
            for (VaultSource source : sources.values()) {
                VaultEntry res = source.getEntry(id);
                if (res != null) return res;
            }
        }
        if (parent != null) return parent.getEntry(id);
        return null;
    }

    @Override
    public VaultEntry getEntry(String name) {
        if (name == null) return null;
        synchronized (sources) {
            for (VaultSource source : sources.values()) {
                VaultEntry res = source.getEntry(name);
                if (res != null) return res;
            }
        }
        if (parent != null) return parent.getEntry(name);
        return null;
    }
}
