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
package de.mhus.lib.core.cfg;

import java.util.function.Consumer;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.MLogUtil;

public abstract class CfgValue<T> {

    private String path;
    private T def;
    private T value;
    private String owner;
    private Consumer<T> updateAction;
    private long updated = 0;
    private String calling;

    public CfgValue(Object owner, String path, T def) {
        if (owner instanceof Class) this.owner = ((Class<?>) owner).getCanonicalName();
        else this.owner = String.valueOf(owner);
        this.path = path;
        this.def = def;
        MApi.getCfgUpdater().register(this);
        update();
        calling = MSystem.findCalling(4);
    }

    public T value() {
        return value == null ? def : value;
    }

    public String getPath() {
        return path;
    }

    public String getOwner() {
        return owner;
    }

    //	public Class<?> getOwnerClass() {
    //		if (owner == null) return null;
    //		if (owner instanceof Class<?>) return (Class<?>)owner;
    //		return owner.getClass();
    //	}

    public T getDefault() {
        return def;
    }

    void update() {
        T newValue = loadValue();
        if (MSystem.equals(value, newValue)) return;
        onPreUpdate(newValue);
        this.value = newValue;
        this.updated = System.currentTimeMillis();
        try {
            onPostUpdate(value);
        } catch (Throwable t) {
            MLogUtil.log().d(this, t);
        }
    }

    protected abstract T loadValue();

    protected abstract T loadValue(String value);

    protected void onPreUpdate(T newValue) {}

    protected void onPostUpdate(T newValue) {
        if (updateAction != null) updateAction.accept(newValue);
    }

    public boolean isOwner(Class<?> name) {
        return owner.equals(name.getCanonicalName());
    }

    public boolean isOwner(String name) {
        return owner.equals(name);
    }

    public void setValue(String v) {
        T newValue = loadValue(v);
        if (newValue == null) return;
        if (MSystem.equals(value, newValue)) return;
        onPreUpdate(newValue);
        this.value = newValue;
        onPostUpdate(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }

    @Override
    public boolean equals(Object in) {
        if (in != null && in instanceof CfgValue<?>) {
            return MSystem.equals(value(), ((CfgValue<?>) in).value());
        }
        return MSystem.equals(value(), in);
    }

    /**
     * Set the onPostUpdate action. The action will be executed on every update.
     * Use (v) is the new Value.
     * 
     * @param consumer
     * @return the called object
     */
    @SuppressWarnings("unchecked")
    public <C extends CfgValue<T>> C updateAction(Consumer<T> consumer) {
        this.updateAction = consumer;
        return (C) this;
    }

    public long getUpdated() {
        return updated;
    }

    public String getCalling() {
        return calling;
    }
}
