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
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import de.mhus.lib.core.util.MObject;

public class MHttpClientBuilder extends MObject {

    private CloseableHttpClient hc;
    private String proxyHost;
    private int proxyPort = 3128;
    private BasicCookieStore cookieStore;
    private boolean useSystemProperties;
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

    protected void configureConnectionManager(HttpClientBuilder build) {
        try { // XXX Legacy to httpclient 4.3.6
            build.setConnectionManagerShared(false);
        } catch (NoSuchMethodError e) {
            
        };
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
}
