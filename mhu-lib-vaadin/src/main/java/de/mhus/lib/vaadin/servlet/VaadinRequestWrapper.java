/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin.servlet;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import com.vaadin.server.VaadinRequest;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.util.EnumerationIterator;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.servlet.RequestWrapper;

public class VaadinRequestWrapper implements RequestWrapper {
	
	private VaadinRequest instance;
	
	public VaadinRequestWrapper(VaadinRequest instance) {
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
		return new EnumerationIterator<String>( instance.getAttributeNames() );
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
		return new EnumerationIterator<String>( instance.getHeaderNames() );
	}

	@Override
	public int getIntHeader(String name) {
		return Integer.valueOf(instance.getHeader(name));
	}

	@Override
	public Iterator<String> getParameterNames() {
		return instance.getParameterMap().keySet().iterator();
	}

	@Override
	public String getMethod() {
		return instance.getMethod();
	}

	@Override
	public String[] getParameterValues(String name) {
		return instance.getParameterMap().values().toArray(new String[0]);
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
		throw new NotSupportedException();
	}

	@Override
	public String getServerName() {
		throw new NotSupportedException();
	}

	@Override
	public int getServerPort() {
		throw new NotSupportedException();
	}

	@Override
	public String getQueryString() {
		throw new NotSupportedException();
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
		return instance.getWrappedSession().getId();
	}

	@Override
	public boolean isSecure() {
		return instance.isSecure();
	}

	@Override
	public IProperties getSession(boolean create) {
		return new VaadinWrappedSessionWrapper( instance.getWrappedSession(create) );
	}

	@Override
	public int getRemotePort() {
		return instance.getRemotePort();
	}

	@Override
	public ServletContext getServletContext() {
		throw new NotSupportedException();
	}

	@Override
	public IProperties getSession() {
		return getSession(true);
	}

	@Override
	public String getSessionId() {
		return instance.getWrappedSession().getId();
	}
	
}
