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
package de.mhus.lib.core.keychain;

import java.util.UUID;

public interface KeychainSource {

    /**
     * Return a entry by id or null if not found.
     *
     * @param id
     * @return The id or null
     */
    KeyEntry getEntry(UUID id);

    /**
     * Return a not editable list of current stored entry ids.
     *
     * @return a list of ids.
     */
    Iterable<UUID> getEntryIds();

    /**
     * Return a unique name of the source.
     *
     * @return the name
     */
    String getName();

    /**
     * Return a editable instance or null if not supported
     *
     * @return editable vault source
     */
    MutableVaultSource getEditable();

    /**
     * Return a entry by name or null if not found. Return the first entry found.
     *
     * @param name
     * @return The id or null
     */
    KeyEntry getEntry(String name);
}
