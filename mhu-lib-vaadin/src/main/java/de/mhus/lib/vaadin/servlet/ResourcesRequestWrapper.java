package de.mhus.lib.vaadin.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ResourcesRequestWrapper implements HttpServletRequest {
	private HttpServletRequest parent;
	private String path;
 
	public ResourcesRequestWrapper(HttpServletRequest request, String path) {
		parent = request;
		this.path = path;
	}

	public Object getAttribute(String name) {
		return parent.getAttribute(name);
	}

	public String getAuthType() {
		return parent.getAuthType();
	}

	public Cookie[] getCookies() {
		return parent.getCookies();
	}

	public Enumeration getAttributeNames() {
		return parent.getAttributeNames();
	}

	public long getDateHeader(String name) {
		return parent.getDateHeader(name);
	}

	public String getCharacterEncoding() {
		return parent.getCharacterEncoding();
	}

	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		parent.setCharacterEncoding(env);
	}

	public String getHeader(String name) {
		return parent.getHeader(name);
	}

	public int getContentLength() {
		return parent.getContentLength();
	}

	public String getContentType() {
		return parent.getContentType();
	}

	public Enumeration getHeaders(String name) {
		return parent.getHeaders(name);
	}

	public ServletInputStream getInputStream() throws IOException {
		return parent.getInputStream();
	}

	public String getParameter(String name) {
		return parent.getParameter(name);
	}

	public Enumeration getHeaderNames() {
		return parent.getHeaderNames();
	}

	public int getIntHeader(String name) {
		return parent.getIntHeader(name);
	}

	public Enumeration getParameterNames() {
		return parent.getParameterNames();
	}

	public String getMethod() {
		return parent.getMethod();
	}

	public String[] getParameterValues(String name) {
		return parent.getParameterValues(name);
	}

	public String getPathInfo() {
		return parent.getPathInfo();
	}

	public Map getParameterMap() {
		return parent.getParameterMap();
	}

	public String getPathTranslated() {
		return parent.getPathTranslated();
	}

	public String getProtocol() {
		return parent.getProtocol();
	}

	public String getScheme() {
		return parent.getScheme();
	}

	public String getContextPath() {
		return parent.getContextPath();
	}

	public String getServerName() {
		return parent.getServerName();
	}

	public String getQueryString() {
		return parent.getQueryString();
	}

	public int getServerPort() {
		return parent.getServerPort();
	}

	public BufferedReader getReader() throws IOException {
		return parent.getReader();
	}

	public String getRemoteUser() {
		return parent.getRemoteUser();
	}

	public boolean isUserInRole(String role) {
		return parent.isUserInRole(role);
	}

	public String getRemoteAddr() {
		return parent.getRemoteAddr();
	}

	public String getRemoteHost() {
		return parent.getRemoteHost();
	}

	public Principal getUserPrincipal() {
		return parent.getUserPrincipal();
	}

	public String getRequestedSessionId() {
		return parent.getRequestedSessionId();
	}

	public void setAttribute(String name, Object o) {
		parent.setAttribute(name, o);
	}

	public String getRequestURI() {
		return cut(parent.getRequestURI());
	}

	private String cut(String in) {
		if (in == null || in.length() < path.length()) return in;
		in = in.substring(path.length());
		return in;
	}

	public void removeAttribute(String name) {
		parent.removeAttribute(name);
	}

	public Locale getLocale() {
		return parent.getLocale();
	}

	public StringBuffer getRequestURL() {
		return parent.getRequestURL();
	}

	public Enumeration getLocales() {
		return parent.getLocales();
	}

	public String getServletPath() {
		return parent.getServletPath();
	}

	public boolean isSecure() {
		return parent.isSecure();
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return parent.getRequestDispatcher(path);
	}

	public HttpSession getSession(boolean create) {
		return parent.getSession(create);
	}

	public HttpSession getSession() {
		return parent.getSession();
	}

	public String getRealPath(String path) {
		return parent.getRealPath(path);
	}

	public int getRemotePort() {
		return parent.getRemotePort();
	}

	public boolean isRequestedSessionIdValid() {
		return parent.isRequestedSessionIdValid();
	}

	public String getLocalName() {
		return parent.getLocalName();
	}

	public boolean isRequestedSessionIdFromCookie() {
		return parent.isRequestedSessionIdFromCookie();
	}

	public String getLocalAddr() {
		return parent.getLocalAddr();
	}

	public boolean isRequestedSessionIdFromURL() {
		return parent.isRequestedSessionIdFromURL();
	}

	public int getLocalPort() {
		return parent.getLocalPort();
	}

	public boolean isRequestedSessionIdFromUrl() {
		return parent.isRequestedSessionIdFromUrl();
	}
	
}
