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
package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefDefinition;

public class FmLayout3x33 extends IFmElement {

    private static final long serialVersionUID = 1L;

    public FmLayout3x33(IDefDefinition... definitions) {
        this("", "", "", definitions);
    }

    public FmLayout3x33(
            String name, String title, String description, IDefDefinition... definitions) {
        super(name, new FaNls(title, description));
        addDefinition(new DefAttribute("layout", "33x33x33"));
        setString("type", "layout33x33x33");
        addDefinition(definitions);
    }
}
