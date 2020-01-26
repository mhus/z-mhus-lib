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
package de.mhus.lib.core.security;

import java.util.Date;
import java.util.UUID;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.NotSupportedException;

public interface Account extends Rightful {

    String MAP_ADMIN = "admin";
    String ACT_READ = "read";
    String ACT_CREATE = "create";
    String ACT_UPDATE = "update";
    String ACT_MODIFY = "modify";
    String ACT_DELETE = "delete";

    boolean isValid();

    boolean validatePassword(String password);

    boolean isSynthetic();

    String getDisplayName();

    /**
     * Gets all attributes of the user account. If attributes are not supported return null;
     *
     * @return all attributes in read only properties list or null if properties are not supported.
     */
    IReadProperties getAttributes();

    /**
     * Overwrites the given attributes, leaves all other properties untouched. Use NullValue to
     * remove a attribute from list.
     *
     * @param properties
     * @throws NotSupportedException Thrown if attributes are not supported
     */
    void putAttributes(IReadProperties properties) throws NotSupportedException;

    /**
     * Return the set of assigned groups
     *
     * @return List of groups
     * @throws NotSupportedException
     */
    String[] getGroups() throws NotSupportedException;

    /**
     * Reload the account data
     *
     * @return true if the reload was possible
     */
    boolean reloadAccount();

    /**
     * Return the user creation date or null if not supported.
     *
     * @return Creation date
     */
    Date getCreationDate();

    /**
     * Return the user modify date or null if not supported.
     *
     * @return Modify date
     */
    Date getModifyDate();

    /**
     * Return the users UUID or null if not supported.
     *
     * @return Unique id of the user
     */
    UUID getUUID();

    /**
     * Return true if the user is active and the user can log in.
     *
     * @return True if the user can log in.
     */
    boolean isActive();
}
