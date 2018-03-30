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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseWrapper implements ResponseWrapper {

	private HttpServletResponse instance;

	public HttpServletResponseWrapper(HttpServletResponse instance) {
		this.instance = instance;
	}
	
	public void addCookie(Cookie cookie) {
		instance.addCookie(cookie);
	}

	public boolean containsHeader(String name) {
		return instance.containsHeader(name);
	}

	public String encodeURL(String url) {
		return instance.encodeURL(url);
	}

	public String getCharacterEncoding() {
		return instance.getCharacterEncoding();
	}

	public String encodeRedirectURL(String url) {
		return instance.encodeRedirectURL(url);
	}

	@Override
	public String getContentType() {
		return instance.getContentType();
	}

	public String encodeUrl(String url) {
		return instance.encodeUrl(url);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return instance.getOutputStream();
	}

	public String encodeRedirectUrl(String url) {
		return instance.encodeRedirectUrl(url);
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		instance.sendError(sc, msg);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return instance.getWriter();
	}

	@Override
	public void sendError(int sc) throws IOException {
		instance.sendError(sc);
	}

	@Override
	public void setCharacterEncoding(String charset) {
		instance.setCharacterEncoding(charset);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		instance.sendRedirect(location);
	}

	public void setContentLength(int len) {
		instance.setContentLength(len);
	}

	public void setContentLengthLong(long len) {
		instance.setContentLengthLong(len);
	}

	@Override
	public void setDateHeader(String name, long date) {
		instance.setDateHeader(name, date);
	}

	@Override
	public void setContentType(String type) {
		instance.setContentType(type);
	}

	@Override
	public void addDateHeader(String name, long date) {
		instance.addDateHeader(name, date);
	}

	@Override
	public void setHeader(String name, String value) {
		instance.setHeader(name, value);
	}

	public void setBufferSize(int size) {
		instance.setBufferSize(size);
	}

	@Override
	public void addHeader(String name, String value) {
		instance.addHeader(name, value);
	}

	@Override
	public void setIntHeader(String name, int value) {
		instance.setIntHeader(name, value);
	}

	public int getBufferSize() {
		return instance.getBufferSize();
	}

	@Override
	public void addIntHeader(String name, int value) {
		instance.addIntHeader(name, value);
	}

	@Override
	public void flushBuffer() throws IOException {
		instance.flushBuffer();
	}

	@Override
	public void setStatus(int sc) {
		instance.setStatus(sc);
	}

	public void resetBuffer() {
		instance.resetBuffer();
	}

	@Override
	public boolean isCommitted() {
		return instance.isCommitted();
	}

	@Override
	public void setStatus(int sc, String sm) {
		instance.setStatus(sc, sm);
	}

	public void reset() {
		instance.reset();
	}

	@Override
	public int getStatus() {
		return instance.getStatus();
	}

	@Override
	public String getHeader(String name) {
		return instance.getHeader(name);
	}

	@Override
	public void setLocale(Locale loc) {
		instance.setLocale(loc);
	}

	@Override
	public Collection<String> getHeaders(String name) {
		return instance.getHeaders(name);
	}

	@Override
	public Collection<String> getHeaderNames() {
		return instance.getHeaderNames();
	}

	@Override
	public Locale getLocale() {
		return instance.getLocale();
	}
	
}
