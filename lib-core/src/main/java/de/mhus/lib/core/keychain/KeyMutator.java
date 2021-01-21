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
package de.mhus.lib.core.keychain;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.errors.NotSupportedException;

@DefaultImplementation(DefaultVaultMutator.class)
public interface KeyMutator {

    /**
     * Try to adapt the entry to the given class or interface.
     *
     * @param entry
     * @param ifc
     * @return The requested interface or class.
     * @throws NotSupportedException Thrown if the entry can't be adapted to the interface.
     * @throws ParseException
     */
    <T> T adaptTo(KeyEntry entry, Class<? extends T> ifc)
            throws ParseException, NotSupportedException;
}
