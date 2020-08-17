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
package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

public class DefRoot extends DefComponent {

    private static final long serialVersionUID = 1L;
    public static final String ROOT = "root";
    private boolean build = false;

    public DefRoot(IDefDefinition... definitions) {
        this(ROOT, definitions);
    }

    public DefRoot(String name, IDefDefinition... definitions) {
        super(name, definitions);
    }

    @Override
    public void inject(DefComponent parent) throws MException {
        throw new MException("can't link root into another container");
    }

    public synchronized DefRoot build() throws MException {
        if (build) return this;
        build = true;
        super.inject(null);
        return this;
    }

    public boolean isBuild() {
        return build;
    }
}
