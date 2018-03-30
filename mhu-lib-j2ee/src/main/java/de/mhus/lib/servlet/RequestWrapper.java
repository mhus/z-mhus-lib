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

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import de.mhus.lib.core.IProperties;

public interface RequestWrapper {

	Object getAttribute(String name);

	Cookie[] getCookies();

	Iterator<String> getAttributeNames();

	long getDateHeader(String name);

	String getCharacterEncoding();

	String getHeader(String name);

	Enumeration<String> getHeaders(String name);

	String getParameter(String name);

	Iterator<String> getHeaderNames();

	int getIntHeader(String name);

	Iterator<String> getParameterNames();

	String getMethod();

	String[] getParameterValues(String name);

	String getPathInfo();

	Map<String, String[]> getParameterMap();

	String getProtocol();

	String getServerName();

	int getServerPort();

	String getQueryString();

	String getRemoteUser();

	String getRemoteAddr();

	String getRemoteHost();

	void setAttribute(String name, Object o);

	void removeAttribute(String name);

	String getRequestedSessionId();

	boolean isSecure();

	IProperties getSession(boolean create);

	int getRemotePort();

	ServletContext getServletContext();

	IProperties getSession();

	String getSessionId();

}
