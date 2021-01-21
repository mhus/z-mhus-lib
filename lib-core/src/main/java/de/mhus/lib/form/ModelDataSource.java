/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.form;

import de.mhus.lib.core.util.MNls;

public class ModelDataSource implements DataSource {

    private DataSource next;

    public ModelDataSource() {}

    public ModelDataSource(DataSource next) {
        setNext(next);
    }

    @Override
    public boolean getBoolean(UiComponent component, String name, boolean def) {
        if (isHandled(name) && component.getConfig().isProperty(name))
            return component.getConfig().getBoolean(name, def);

        if (next != null) return next.getBoolean(component, name, def);

        return def;
    }

    private boolean isHandled(String name) {
        switch (name) {
            case DataSource.CAPTION:
            case DataSource.DESCRIPTION:
            case DataSource.EDITOR_EDITABLE:
            case DataSource.ENABLED:
            case DataSource.VISIBLE:
                return true;
        }
        return false;
    }

    @Override
    public int getInt(UiComponent component, String name, int def) {
        if (isHandled(name) && component.getConfig().isProperty(name))
            return component.getConfig().getInt(name, def);

        if (next != null) return next.getInt(component, name, def);

        return def;
    }

    @Override
    public String getString(UiComponent component, String name, String def) {
        if (isHandled(name) && component.getConfig().isProperty(name)) {

            String expression = component.getConfig().getString(name, def);
            return MNls.find(component.getForm(), expression);
        }
        if (next != null) return next.getString(component, name, def);

        return def;
    }

    @Override
    public Object getObject(UiComponent component, String name, Object def) {
        if (isHandled(name) && component.getConfig().isProperty(name))
            return component.getConfig().getString(name, def != null ? String.valueOf(def) : null);

        if (next != null) return next.getObject(component, name, def);

        return def;
    }

    @Override
    public Object getObject(String name, Object def) {
        if (next != null) return next.getObject(name, def);

        return def;
    }

    @Override
    public void setObject(UiComponent component, String name, Object value) throws Exception {
        if (next != null) next.setObject(component, name, value);
    }

    @Override
    public DataSource getNext() {
        return next;
    }

    public void setNext(DataSource chain) {
        this.next = chain;
    }
}
