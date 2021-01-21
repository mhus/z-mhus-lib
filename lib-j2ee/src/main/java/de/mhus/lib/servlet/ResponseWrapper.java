/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
