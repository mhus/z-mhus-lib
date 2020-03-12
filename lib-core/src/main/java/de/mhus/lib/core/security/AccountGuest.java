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
import java.util.HashSet;
import java.util.UUID;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.security.Account;
import de.mhus.lib.errors.NotSupportedException;

public class AccountGuest implements Account {

    private String name = AccessApi.GUEST_NAME;
    private HashSet<String> groups = new HashSet<>();
    private IProperties properties = new MProperties();

    @Override
    public String getName() {
        return AccessApi.GUEST_NAME;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean validatePassword(String password) {
        return true;
    }

    @Override
    public String toString() {
        return AccessApi.GUEST_NAME;
    }

    @Override
    public boolean isSynthetic() {
        return true;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasGroup(String group) {
        return groups.contains(group);
    }

    @Override
    public String[] getGroups() {
        return groups.toArray(new String[groups.size()]);
    }

    @Override
    public IReadProperties getAttributes() {
        return properties;
    }

    @Override
    public void putAttributes(IReadProperties properties) throws NotSupportedException {
        // Special behavior - attributes are read only
    }

    @Override
    public boolean reloadAccount() {
        return false;
    }

    @Override
    public Date getCreationDate() {
        return null;
    }

    @Override
    public Date getModifyDate() {
        return null;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
