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
package de.mhus.lib.core.shiro;

import java.util.Date;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.security.Account;
import de.mhus.lib.errors.NotSupportedException;

public class ShiroAccount implements Account {

    private Subject subject;

    public ShiroAccount(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean hasGroup(String group) {
        return subject.hasRole(group);
    }

    @Override
    public String getName() {
        return AccessUtil.getPrincipal(subject);
    }

    @Override
    public boolean isValid() {
        return subject.isAuthenticated();
    }

    @Override
    public boolean validatePassword(String password) {
        throw new NotSupportedException();
    }

    @Override
    public boolean isSynthetic() {
        return false;
    }

    @Override
    public String getDisplayName() {
        return AccessUtil.getPrincipalData().getDisplayName();
    }

    @Override
    public IReadProperties getAttributes() {
        Session session = subject.getSession(false);
        MProperties out = new MProperties();
        if (session != null) {
            for (Object key : session.getAttributeKeys())
                out.put(key.toString(), session.getAttribute(key));
        }
        return out;
    }

    @Override
    public void putAttributes(IReadProperties properties) throws NotSupportedException {
        Session session = subject.getSession();
        for (Entry<String, Object> entry : properties.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String[] getGroups() throws NotSupportedException {
        throw new NotSupportedException();
    }

    @Override
    public boolean reloadAccount() {
        return true;
    }

    @Override
    public Date getCreationDate() {
        return null; // TODO
    }

    @Override
    public Date getModifyDate() {
        return null; // TODO
    }

    @Override
    public UUID getUUID() {
        return null; // TODO
    }

    @Override
    public boolean isActive() {
        return true; // TODO
    }
}
