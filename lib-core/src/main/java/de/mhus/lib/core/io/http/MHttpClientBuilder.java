/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.io.http;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.RequestLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import de.mhus.lib.core.logging.ITracer;
import de.mhus.lib.core.util.MObject;
import io.opentracing.Span;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.tag.Tags;

public class MHttpClientBuilder extends MObject {

    private CloseableHttpClient hc;
    private String proxyHost;
    private int proxyPort = 3128;
    private BasicCookieStore cookieStore;
    private boolean useSystemProperties;
    private boolean tracing = true;
    protected HttpClientConnectionManager connManager;

    /**
     * The function will return the http client. If the http client not exists or was closed the
     * function builds a new client.
     *
     * @return the client
     */
    public HttpClient getHttpClient() {
        synchronized (this) {
            if (hc == null) {
                HttpClientBuilder build = HttpClients.custom();
                configureTrace(build);
                configureConnectionManager(build);
                configureProxy(build);
                configureCookieStore(build);
                configureProtocolHandling(build);
                configureBuilder(build);
                hc = build.build();
            }
        }
        return hc;
    }

    protected void configureTrace(HttpClientBuilder build) {
        if (tracing) {
            build.addInterceptorFirst(
                    new HttpRequestInterceptor() {

                        @Override
                        public void process(HttpRequest request, HttpContext context)
                                throws HttpException, IOException {
                            try {
                                Span span = ITracer.get().current();
                                if (span != null) {
                                    RequestLine line = request.getRequestLine();
                                    Tags.SPAN_KIND.set(span, Tags.SPAN_KIND_CLIENT);
                                    Tags.HTTP_URL.set(span, line.getUri());
                                    Tags.HTTP_METHOD.set(span, line.getMethod());
                                    ITracer.get()
                                            .tracer()
                                            .inject(
                                                    span.context(),
                                                    Format.Builtin.HTTP_HEADERS,
                                                    new TextMap() {

                                                        @Override
                                                        public Iterator<Entry<String, String>>
                                                                iterator() {
                                                            return null;
                                                        }

                                                        @Override
                                                        public void put(String key, String value) {
                                                            request.setHeader(key, value);
                                                        }
                                                    });
                                }
                            } catch (Throwable t) {
                            }
                        }
                    });
            build.addInterceptorLast(
                    new HttpResponseInterceptor() {

                        @Override
                        public void process(HttpResponse response, HttpContext context)
                                throws HttpException, IOException {
                            try {
                                Span span = ITracer.get().current();
                                if (span != null) {
                                    Tags.HTTP_STATUS.set(
                                            span, response.getStatusLine().getStatusCode());
                                }
                            } catch (Throwable t) {
                            }
                        }
                    });
        }
    }

    protected void configureConnectionManager(HttpClientBuilder build) {
        try { // XXX Legacy to httpclient 4.3.6
            build.setConnectionManagerShared(false);
        } catch (NoSuchMethodError e) {

        }
        ;
        connManager = new PoolingHttpClientConnectionManager();
        ((PoolingHttpClientConnectionManager) connManager).setMaxTotal(100);
        ((PoolingHttpClientConnectionManager) connManager).setDefaultMaxPerRoute(100);
        build.setConnectionManager(connManager);
    }

    /**
     * Overwrite to customize client builder.
     *
     * @param build
     */
    protected void configureBuilder(HttpClientBuilder build) {}

    public void close() {
        synchronized (this) {
            if (hc != null)
                try {
                    hc.close();
                } catch (IOException e) {
                }
            hc = null;
            if (connManager != null) connManager.closeIdleConnections(0, TimeUnit.NANOSECONDS);
            connManager = null;
        }
    }

    public boolean exists() {
        return hc != null;
    }

    protected void configureProtocolHandling(HttpClientBuilder build) {
        if (useSystemProperties) build.useSystemProperties();
    }

    protected void configureCookieStore(HttpClientBuilder build) {
        if (cookieStore == null) cookieStore = new BasicCookieStore();
        build.setDefaultCookieStore(cookieStore);
    }

    protected void configureProxy(HttpClientBuilder build) {
        if (proxyHost != null) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            build.setRoutePlanner(routePlanner);
        }
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public MHttpClientBuilder setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public MHttpClientBuilder setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    public MHttpClientBuilder setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    public boolean isUseSystemProperties() {
        return useSystemProperties;
    }

    public MHttpClientBuilder setUseSystemProperties(boolean useSystemProperties) {
        this.useSystemProperties = useSystemProperties;
        return this;
    }

    //	public HttpResponse execute(HttpGet action) throws ClientProtocolException, IOException {
    //		return getHttpClient().execute(action);
    //	}
    //
    //	public HttpResponse execute(HttpPost action) throws ClientProtocolException, IOException {
    //		return getHttpClient().execute(action);
    //	}
    //
    //	public HttpResponse execute(HttpPut action) throws ClientProtocolException, IOException {
    //		return getHttpClient().execute(action);
    //	}
    //
    //	public HttpResponse execute(HttpDelete action) throws ClientProtocolException, IOException {
    //		return getHttpClient().execute(action);
    //	}

    public void cleanup() {
        synchronized (this) {
            if (connManager != null) connManager.closeIdleConnections(1, TimeUnit.SECONDS);
        }
    }

    public static void close(HttpResponse response) {
        if (response == null || !(response instanceof CloseableHttpResponse)) return;
        try {
            ((CloseableHttpResponse) response).close();
        } catch (IOException e) {
            //			log().d(e);
        }
    }

    public static void consume(HttpEntity entity) {
        if (entity == null) return;
        try {
            EntityUtils.consume(entity);
        } catch (IOException e) {
            //			log().d(e);
        }
    }

    public boolean isTracing() {
        return tracing;
    }

    public MHttpClientBuilder setTracing(boolean tracing) {
        this.tracing = tracing;
        return this;
    }
}
