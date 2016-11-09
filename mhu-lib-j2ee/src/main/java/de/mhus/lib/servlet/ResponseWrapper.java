package de.mhus.lib.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

public interface ResponseWrapper {

	String getContentType();

	ServletOutputStream getOutputStream() throws IOException;

	void sendError(int sc, String msg) throws IOException;

	PrintWriter getWriter() throws IOException;

	void sendError(int sc) throws IOException;

	void setCharacterEncoding(String charset);

	void sendRedirect(String location) throws IOException;

	void setDateHeader(String name, long date);

	void setContentType(String type);

	void addDateHeader(String name, long date);

	void setHeader(String name, String value);

	void addHeader(String name, String value);

	void setIntHeader(String name, int value);

	void addIntHeader(String name, int value);

	void flushBuffer() throws IOException;

	void setStatus(int sc);

	boolean isCommitted();

	void setStatus(int sc, String sm);

	int getStatus();

	String getHeader(String name);

	void setLocale(Locale loc);

	Collection<String> getHeaders(String name);

	Collection<String> getHeaderNames();

	Locale getLocale();

}
