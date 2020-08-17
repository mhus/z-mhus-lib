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
package de.mhus.lib.form.definition;

import java.util.UUID;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;

public class FaNls extends DefAttribute {

    private String title;
    private String description;

    public FaNls(String value) {
        this(value, null, null);
    }

    public FaNls(String title, String description) {
        this(null, title, description);
    }

    public FaNls(String value, String title, String description) {
        super("nls", value == null ? UUID.randomUUID().toString() : value);
        this.title = title;
        this.description = description;
    }

    @Override
    public void inject(DefComponent root) throws MException {
        super.inject(root);
        if (title != null) root.setString("caption", title);
        if (description != null) root.setString("description", description);
    }

    @Override
    public String toString() {
        return getName() + "->[" + title + "," + description + "]";
    }
}
