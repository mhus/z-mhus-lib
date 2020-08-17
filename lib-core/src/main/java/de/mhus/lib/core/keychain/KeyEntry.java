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

import de.mhus.lib.core.util.SecureString;

public interface KeyEntry {

    /**
     * Returns the unique id of the entry.
     *
     * @return The unique id
     */
    UUID getId();

    /**
     * Returns the type of the entry as string. A list of default types is defined in MVault.
     *
     * @return The type of the entry, never null.
     */
    String getType();

    /**
     * Return a readable description describe the key and/or the usage.
     *
     * @return description
     */
    String getDescription();

    /**
     * Return the value of the entry as text.
     *
     * @return The entry as text.
     */
    SecureString getValue();

    /**
     * Return a technical name of the entry.
     *
     * @return The name
     */
    String getName();
}
