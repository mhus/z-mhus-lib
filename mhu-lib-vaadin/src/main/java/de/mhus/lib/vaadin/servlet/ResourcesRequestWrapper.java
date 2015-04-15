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

	@Override
	public Object getAttribute(String name) {
		return parent.getAttribute(name);
	}

	@Override
	public String getAuthType() {
		return parent.getAuthType();
	}

	@Override
	public Cookie[] getCookies() {
		return parent.getCookies();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames() {
		return parent.getAttributeNames();
	}

	@Override
	public long getDateHeader(String name) {
		return parent.getDateHeader(name);
	}

	@Override
	public String getCharacterEncoding() {
		return parent.getCharacterEncoding();
	}

	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		parent.setCharacterEncoding(env);
	}

	@Override
	public String getHeader(String name) {
		return parent.getHeader(name);
	}

	@Override
	public int getContentLength() {
		return parent.getContentLength();
	}

	@Override
	public String getContentType() {
		return parent.getContentType();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaders(String name) {
		return parent.getHeaders(name);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return parent.getInputStream();
	}

	@Override
	public String getParameter(String name) {
		return parent.getParameter(name);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaderNames() {
		return parent.getHeaderNames();
	}

	@Override
	public int getIntHeader(String name) {
		return parent.getIntHeader(name);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Enumeration getParameterNames() {
		return parent.getParameterNames();
	}

	@Override
	public String getMethod() {
		return parent.getMethod();
	}

	@Override
	public String[] getParameterValues(String name) {
		return parent.getParameterValues(name);
	}

	@Override
	public String getPathInfo() {
		return parent.getPathInfo();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getParameterMap() {
		return parent.getParameterMap();
	}

	@Override
	public String getPathTranslated() {
		return parent.getPathTranslated();
	}

	@Override
	public String getProtocol() {
		return parent.getProtocol();
	}

	@Override
	public String getScheme() {
		return parent.getScheme();
	}

	@Override
	public String getContextPath() {
		return parent.getContextPath();
	}

	@Override
	public String getServerName() {
		return parent.getServerName();
	}

	@Override
	public String getQueryString() {
		return parent.getQueryString();
	}

	@Override
	public int getServerPort() {
		return parent.getServerPort();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return parent.getReader();
	}

	@Override
	public String getRemoteUser() {
		return parent.getRemoteUser();
	}

	@Override
	public boolean isUserInRole(String role) {
		return parent.isUserInRole(role);
	}

	@Override
	public String getRemoteAddr() {
		return parent.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return parent.getRemoteHost();
	}

	@Override
	public Principal getUserPrincipal() {
		return parent.getUserPrincipal();
	}

	@Override
	public String getRequestedSessionId() {
		return parent.getRequestedSessionId();
	}

	@Override
	public void setAttribute(String name, Object o) {
		parent.setAttribute(name, o);
	}

	@Override
	public String getRequestURI() {
		return cut(parent.getRequestURI());
	}

	private String cut(String in) {
		if (in == null || in.length() < path.length()) return in;
		in = in.substring(path.length());
		return in;
	}

	@Override
	public void removeAttribute(String name) {
		parent.removeAttribute(name);
	}

	@Override
	public Locale getLocale() {
		return parent.getLocale();
	}

	@Override
	public StringBuffer getRequestURL() {
		return parent.getRequestURL();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getLocales() {
		return parent.getLocales();
	}

	@Override
	public String getServletPath() {
		return parent.getServletPath();
	}

	@Override
	public boolean isSecure() {
		return parent.isSecure();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return parent.getRequestDispatcher(path);
	}

	@Override
	public HttpSession getSession(boolean create) {
		return parent.getSession(create);
	}

	@Override
	public HttpSession getSession() {
		return parent.getSession();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getRealPath(String path) {
		return parent.getRealPath(path);
	}

	@Override
	public int getRemotePort() {
		return parent.getRemotePort();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return parent.isRequestedSessionIdValid();
	}

	@Override
	public String getLocalName() {
		return parent.getLocalName();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return parent.isRequestedSessionIdFromCookie();
	}

	@Override
	public String getLocalAddr() {
		return parent.getLocalAddr();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return parent.isRequestedSessionIdFromURL();
	}

	@Override
	public int getLocalPort() {
		return parent.getLocalPort();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return parent.isRequestedSessionIdFromUrl();
	}

}
