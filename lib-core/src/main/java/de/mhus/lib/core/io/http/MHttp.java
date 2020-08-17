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
package de.mhus.lib.core.io.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MFile;

public class MHttp {

    public static final Map<Integer, String> HTTP_STATUS_CODES =
            Collections.unmodifiableMap(
                    new HashMap<Integer, String>() {
                        private static final long serialVersionUID = 1L;

                        {
                            put(100, "Continue");
                            put(101, "Switching Protocols");
                            put(200, "OK");
                            put(201, "Created");
                            put(202, "Accepted");
                            put(203, "Non-Authoritative Information");
                            put(204, "No Content");
                            put(205, "Reset Content");
                            put(300, "Multiple Choices");
                            put(301, "Moved Permanently");
                            put(302, "Found");
                            put(303, "See Other");
                            put(304, "Not Modified");
                            put(305, "Use Proxy");
                            put(307, "Temporary Redirect");
                            put(400, "Bad Request");
                            put(401, "Unauthorized");
                            put(402, "Payment Required");
                            put(403, "Forbidden");
                            put(404, "Not Found");
                            put(405, "Method Not Allowed");
                            put(406, "Not Acceptable");
                            put(407, "Proxy Authentication Required");
                            put(408, "Request Time-out");
                            put(409, "Conflict");
                            put(410, "Gone");
                            put(411, "Length Required");
                            put(412, "Precondition Failed");
                            put(413, "Request Entity Too Large");
                            put(414, "Request-URI Too Large");
                            put(415, "Unsupported Media Type");
                            put(416, "Requested range not satisfiable");
                            put(417, "SlimExpectation Failed");
                            put(500, "Internal Server Error");
                            put(501, "Not Implemented");
                            put(502, "Bad Gateway");
                            put(503, "Service Unavailable");
                            put(504, "Gateway Time-out");
                            put(505, "HTTP Version not supported");
                        }
                    });

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String CONTENT_TYPE_PDF = "application/pdf";

    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_CONNECT = "CONNECT";

    public enum METHOD {
        GET,
        HEAD,
        POST,
        PUT,
        DELETE,
        CONNECT,
        OPTIONS,
        TRACE,
        PATCH
    }

    private static MHttpClientBuilder client;

    public static METHOD toMethod(String in) {
        return METHOD.valueOf(in.trim().toUpperCase());
    }

    public static String getContentType(String extension) {
        return MFile.getMimeType(extension);
    }

    /**
     * Provide a central http client for common use. It will be configured with system proxy and
     * usual parameters
     *
     * @return the shared configured http client
     */
    public static synchronized HttpClient getSharedClient() {
        if (client == null) {
            client =
                    new MHttpClientBuilder() {
                        @Override
                        protected void configureConnectionManager(HttpClientBuilder build) {
                            super.configureConnectionManager(build);
                            ((PoolingHttpClientConnectionManager) connManager).setMaxTotal(10000);
                            ((PoolingHttpClientConnectionManager) connManager)
                                    .setDefaultMaxPerRoute(10000);
                        }
                    };
            // TODO use als mhus-config configuration for proxy and other configurations
            client.setUseSystemProperties(true);
        }
        client.cleanup();
        return client.getHttpClient();
    }

    public static void setFormParameters(HttpPost action, String... values) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (int i = 0; i < values.length; i = i + 2) {
            formparams.add(new BasicNameValuePair(values[i], values[i + 1]));
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        action.setEntity(entity);
    }

    public static void setFormParameters(HttpPost action, IProperties params) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        params.forEach(
                e ->
                        formparams.add(
                                new BasicNameValuePair(e.getKey(), MCast.toString(e.getValue()))));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        action.setEntity(entity);
    }
}
