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
package de.mhus.lib.servlet;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.util.EnumerationIterator;
import de.mhus.lib.errors.NotSupportedException;

public class PortletRequestWrapper implements RequestWrapper {

	private PortletRequest instance;
	
	PortletRequestWrapper(PortletRequest instance) {
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
		throw new NotSupportedException();
	}

	@Override
	public String getCharacterEncoding() {
		throw new NotSupportedException();
	}

	@Override
	public String getHeader(String name) {
		throw new NotSupportedException();
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		throw new NotSupportedException();
	}

	@Override
	public String getParameter(String name) {
		return instance.getParameter(name);
	}

	@Override
	public Iterator<String> getHeaderNames() {
		throw new NotSupportedException();
	}

	@Override
	public int getIntHeader(String name) {
		throw new NotSupportedException();
	}

	@Override
	public Iterator<String> getParameterNames() {
		return new EnumerationIterator<String>( instance.getParameterNames() );
	}

	@Override
	public String getMethod() {
		throw new NotSupportedException();
	}

	@Override
	public String[] getParameterValues(String name) {
		return instance.getParameterValues(name);
	}

	@Override
	public String getPathInfo() {
		throw new NotSupportedException();
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
		return instance.getServerName();
	}

	@Override
	public int getServerPort() {
		return instance.getServerPort();
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
		throw new NotSupportedException();
	}

	@Override
	public String getRemoteHost() {
		throw new NotSupportedException();
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
		return new PortletSessionWrapper( instance.getPortletSession(create) );
	}

	@Override
	public int getRemotePort() {
		throw new NotSupportedException();
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
		return instance.getPortletSession().getId(); // This is not the http session id ... look for cookie?
	}
	
}
