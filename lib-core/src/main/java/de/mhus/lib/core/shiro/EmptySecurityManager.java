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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

public class EmptySecurityManager implements SecurityManager {

    @Override
    public AuthenticationInfo authenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        return null;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return false;
    }

    @Override
    public boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission) {
        return false;
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection subjectPrincipal, String... permissions) {
        return null;
    }

    @Override
    public boolean[] isPermitted(
            PrincipalCollection subjectPrincipal, List<Permission> permissions) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection subjectPrincipal, String... permissions) {
        return false;
    }

    @Override
    public boolean isPermittedAll(
            PrincipalCollection subjectPrincipal, Collection<Permission> permissions) {
        return false;
    }

    @Override
    public void checkPermission(PrincipalCollection subjectPrincipal, String permission)
            throws AuthorizationException {
        throw new AuthorizationException();
    }

    @Override
    public void checkPermission(PrincipalCollection subjectPrincipal, Permission permission)
            throws AuthorizationException {
        throw new AuthorizationException();
    }

    @Override
    public void checkPermissions(PrincipalCollection subjectPrincipal, String... permissions)
            throws AuthorizationException {
        throw new AuthorizationException();
    }

    @Override
    public void checkPermissions(
            PrincipalCollection subjectPrincipal, Collection<Permission> permissions)
            throws AuthorizationException {
        throw new AuthorizationException();
    }

    @Override
    public boolean hasRole(PrincipalCollection subjectPrincipal, String roleIdentifier) {
        return false;
    }

    @Override
    public boolean[] hasRoles(PrincipalCollection subjectPrincipal, List<String> roleIdentifiers) {
        return new boolean[roleIdentifiers.size()];
    }

    @Override
    public boolean hasAllRoles(
            PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) {
        return false;
    }

    @Override
    public void checkRole(PrincipalCollection subjectPrincipal, String roleIdentifier)
            throws AuthorizationException {
        throw new AuthorizationException();
    }

    @Override
    public void checkRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers)
            throws AuthorizationException {
        throw new AuthorizationException();
    }

    @Override
    public void checkRoles(PrincipalCollection subjectPrincipal, String... roleIdentifiers)
            throws AuthorizationException {
        throw new AuthorizationException();
    }

    @Override
    public Session start(SessionContext context) {
        return new EmptySession(context);
    }

    @Override
    public Session getSession(SessionKey key) throws SessionException {
        return new EmptySession(key);
    }

    @Override
    public Subject login(Subject subject, AuthenticationToken authenticationToken)
            throws AuthenticationException {
        throw new AuthorizationException();
    }

    @Override
    public void logout(Subject subject) {}

    @Override
    public Subject createSubject(SubjectContext context) {
        return new EmptySubject(context);
    }

    private class EmptySession implements Session {

        Serializable id = UUID.randomUUID().toString();
        Date start = new Date();
        Date access = new Date();
        private long timeout;
        HashMap<Object, Object> attributes = new HashMap<>();

        public EmptySession(SessionContext context) {
            id = context.getSessionId();
        }

        public EmptySession(SessionKey key) {
            id = key.getSessionId();
        }

        @Override
        public Serializable getId() {
            return id;
        }

        @Override
        public Date getStartTimestamp() {
            return start;
        }

        @Override
        public Date getLastAccessTime() {
            return access;
        }

        @Override
        public long getTimeout() throws InvalidSessionException {
            return timeout;
        }

        @Override
        public void setTimeout(long maxIdleTimeInMillis) throws InvalidSessionException {
            timeout = maxIdleTimeInMillis;
        }

        @Override
        public String getHost() {
            return null;
        }

        @Override
        public void touch() throws InvalidSessionException {
            access = new Date();
        }

        @Override
        public void stop() throws InvalidSessionException {}

        @Override
        public Collection<Object> getAttributeKeys() throws InvalidSessionException {
            return new ArrayList<>(attributes.keySet());
        }

        @Override
        public Object getAttribute(Object key) throws InvalidSessionException {
            return attributes.get(key);
        }

        @Override
        public void setAttribute(Object key, Object value) throws InvalidSessionException {
            attributes.put(key, value);
        }

        @Override
        public Object removeAttribute(Object key) throws InvalidSessionException {
            return attributes.remove(key);
        }
    }

    private class EmptySubject implements Subject {

        public EmptySubject(SubjectContext context) {
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object getPrincipal() {
            return "none";
        }

        @Override
        public PrincipalCollection getPrincipals() {
            return null;
        }

        @Override
        public boolean isPermitted(String permission) {
            return false;
        }

        @Override
        public boolean isPermitted(Permission permission) {
            return false;
        }

        @Override
        public boolean[] isPermitted(String... permissions) {
            return new boolean[permissions.length];
        }

        @Override
        public boolean[] isPermitted(List<Permission> permissions) {
            return new boolean[permissions.size()];
        }

        @Override
        public boolean isPermittedAll(String... permissions) {
            return false;
        }

        @Override
        public boolean isPermittedAll(Collection<Permission> permissions) {
            return false;
        }

        @Override
        public void checkPermission(String permission) throws AuthorizationException {
            throw new AuthorizationException();
        }

        @Override
        public void checkPermission(Permission permission) throws AuthorizationException {
            throw new AuthorizationException();
        }

        @Override
        public void checkPermissions(String... permissions) throws AuthorizationException {
            throw new AuthorizationException();
        }

        @Override
        public void checkPermissions(Collection<Permission> permissions)
                throws AuthorizationException {
            throw new AuthorizationException();
        }

        @Override
        public boolean hasRole(String roleIdentifier) {
            return false;
        }

        @Override
        public boolean[] hasRoles(List<String> roleIdentifiers) {
            return null;
        }

        @Override
        public boolean hasAllRoles(Collection<String> roleIdentifiers) {
            return false;
        }

        @Override
        public void checkRole(String roleIdentifier) throws AuthorizationException {
            throw new AuthorizationException();
        }

        @Override
        public void checkRoles(Collection<String> roleIdentifiers) throws AuthorizationException {
            throw new AuthorizationException();
        }

        @Override
        public void checkRoles(String... roleIdentifiers) throws AuthorizationException {
            throw new AuthorizationException();
        }

        @Override
        public void login(AuthenticationToken token) throws AuthenticationException {
            throw new AuthorizationException();
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public boolean isRemembered() {
            return false;
        }

        @Override
        public Session getSession() {
            return null;
        }

        @Override
        public Session getSession(boolean create) {
            return null;
        }

        @Override
        public void logout() {}

        @Override
        public <V> V execute(Callable<V> callable) throws ExecutionException {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }

        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }

        @Override
        public <V> Callable<V> associateWith(Callable<V> callable) {
            return callable;
        }

        @Override
        public Runnable associateWith(Runnable runnable) {
            return runnable;
        }

        @Override
        public void runAs(PrincipalCollection principals)
                throws NullPointerException, IllegalStateException {}

        @Override
        public boolean isRunAs() {
            return false;
        }

        @Override
        public PrincipalCollection getPreviousPrincipals() {
            return null;
        }

        @Override
        public PrincipalCollection releaseRunAs() {
            return null;
        }
    }
}
