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

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(ModelDataSource.class)
public interface DataSource {

    public static final String ENABLED = "enabled";
    public static final String EDITABLE = "enabled";
    public static final String VISIBLE = "visible";
    public static final String VALUE = "";
    public static final String CAPTION = "caption";
    public static final String EDITOR_EDITABLE = "editable";
    public static final String DESCRIPTION = "description";
    public static final String ITEMS = "items";

    boolean getBoolean(UiComponent component, String name, boolean def);

    int getInt(UiComponent component, String name, int def);

    String getString(UiComponent component, String name, String def);

    Object getObject(UiComponent component, String name, Object def);

    void setObject(UiComponent component, String name, Object value) throws Exception;

    DataSource getNext();

    Object getObject(String name, Object def);
}
