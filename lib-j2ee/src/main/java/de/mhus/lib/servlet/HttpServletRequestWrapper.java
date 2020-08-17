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
package de.mhus.lib.servlet;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.util.EnumerationIterator;

public class HttpServletRequestWrapper implements RequestWrapper {

    private HttpServletRequest instance;

    public HttpServletRequestWrapper(HttpServletRequest instance) {
        this.instance = instance;
    }

    @Override
    public Object getAttribute(String name) {
        return instance.getAttribute(name);
    }

    @Override
    public Cookie[] getCookies() {
        return instance.getCookies();
    }

    @Override
    public Iterator<String> getAttributeNames() {
        return new EnumerationIterator<String>(instance.getAttributeNames());
    }

    @Override
    public long getDateHeader(String name) {
        return instance.getDateHeader(name);
    }

    @Override
    public String getCharacterEncoding() {
        return instance.getCharacterEncoding();
    }

    @Override
    public String getHeader(String name) {
        return instance.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return instance.getHeaders(name);
    }

    @Override
    public String getParameter(String name) {
        return instance.getParameter(name);
    }

    @Override
    public Iterator<String> getHeaderNames() {
        return new EnumerationIterator<String>(instance.getHeaderNames());
    }

    @Override
    public int getIntHeader(String name) {
        return instance.getIntHeader(name);
    }

    @Override
    public Iterator<String> getParameterNames() {
        return new EnumerationIterator<String>(instance.getParameterNames());
    }

    @Override
    public String getMethod() {
        return instance.getMethod();
    }

    @Override
    public String[] getParameterValues(String name) {
        return instance.getParameterValues(name);
    }

    @Override
    public String getPathInfo() {
        return instance.getPathInfo();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return instance.getParameterMap();
    }

    @Override
    public String getProtocol() {
        return instance.getProtocol();
    }

    @Override
    public String getServerName() {
        return instance.getServerName();
    }

    @Override
    public int getServerPort() {
        return instance.getServerPort();
    }

    @Override
    public String getQueryString() {
        return instance.getQueryString();
    }

    @Override
    public String getRemoteUser() {
        return instance.getRemoteUser();
    }

    @Override
    public String getRemoteAddr() {
        return instance.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return instance.getRemoteHost();
    }

    @Override
    public void setAttribute(String name, Object o) {
        instance.setAttribute(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        instance.removeAttribute(name);
    }

    @Override
    public String getRequestedSessionId() {
        return instance.getRequestedSessionId();
    }

    @Override
    public boolean isSecure() {
        return instance.isSecure();
    }

    @Override
    public IProperties getSession(boolean create) {
        return new HttpSessionWrapper(instance.getSession(create));
    }

    @Override
    public int getRemotePort() {
        return instance.getRemotePort();
    }

    @Override
    public ServletContext getServletContext() {
        return instance.getServletContext();
    }

    @Override
    public IProperties getSession() {
        return getSession(true);
    }

    @Override
    public String getSessionId() {
        return instance.getSession().getId();
    }
}
