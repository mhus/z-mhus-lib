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

public class UidlRequestWrapper implements HttpServletRequest {
	
	HttpServletRequest request;

	public UidlRequestWrapper(HttpServletRequest request) {
		this.request = request;
	}

	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	public String getAuthType() {
		return request.getAuthType();
	}

	public Cookie[] getCookies() {
		return request.getCookies();
	}

	public Enumeration getAttributeNames() {
		return request.getAttributeNames();
	}

	public long getDateHeader(String name) {
		return request.getDateHeader(name);
	}

	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding(env);
	}

	public String getHeader(String name) {
		return request.getHeader(name);
	}

	public int getContentLength() {
		return request.getContentLength();
	}

	public String getContentType() {
		return request.getContentType();
	}

	public Enumeration getHeaders(String name) {
		return request.getHeaders(name);
	}

	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	public String getParameter(String name) {
		return request.getParameter(name);
	}

	public Enumeration getHeaderNames() {
		return request.getHeaderNames();
	}

	public int getIntHeader(String name) {
		return request.getIntHeader(name);
	}

	public Enumeration getParameterNames() {
		return request.getParameterNames();
	}

	public String getMethod() {
		return request.getMethod();
	}

	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	public String getPathInfo() {
		String out = request.getPathInfo();
		out = out.substring(8);
		return out;
	}

	public Map getParameterMap() {
		return request.getParameterMap();
	}

	public String getPathTranslated() {
		return request.getPathTranslated();
	}

	public String getProtocol() {
		return request.getProtocol();
	}

	public String getScheme() {
		return request.getScheme();
	}

	public String getContextPath() {
		return request.getContextPath();
	}

	public String getServerName() {
		return request.getServerName();
	}

	public String getQueryString() {
		return request.getQueryString();
	}

	public int getServerPort() {
		return request.getServerPort();
	}

	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	public boolean isUserInRole(String role) {
		return request.isUserInRole(role);
	}

	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	public Principal getUserPrincipal() {
		return request.getUserPrincipal();
	}

	public String getRequestedSessionId() {
		return request.getRequestedSessionId();
	}

	public void setAttribute(String name, Object o) {
		request.setAttribute(name, o);
	}

	public String getRequestURI() {
		return request.getRequestURI();
	}

	public void removeAttribute(String name) {
		request.removeAttribute(name);
	}

	public Locale getLocale() {
		return request.getLocale();
	}

	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}

	public Enumeration getLocales() {
		return request.getLocales();
	}

	public String getServletPath() {
		return request.getServletPath();
	}

	public boolean isSecure() {
		return request.isSecure();
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return request.getRequestDispatcher(path);
	}

	public HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	public String getRealPath(String path) {
		return request.getRealPath(path);
	}

	public int getRemotePort() {
		return request.getRemotePort();
	}

	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	public String getLocalName() {
		return request.getLocalName();
	}

	public boolean isRequestedSessionIdFromCookie() {
		return request.isRequestedSessionIdFromCookie();
	}

	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}

	public int getLocalPort() {
		return request.getLocalPort();
	}

	public boolean isRequestedSessionIdFromUrl() {
		return request.isRequestedSessionIdFromUrl();
	}
}
