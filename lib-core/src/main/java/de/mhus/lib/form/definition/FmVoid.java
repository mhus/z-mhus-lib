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

import de.mhus.lib.core.definition.IDefAttribute;

public class FmVoid extends IFmElement {

    private static final long serialVersionUID = 1L;

    public FmVoid(IDefAttribute... definitions) {
        super(UUID.randomUUID().toString(), definitions);
        setString("type", "void");
    }
}
