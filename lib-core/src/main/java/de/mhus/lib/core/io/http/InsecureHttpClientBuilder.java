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
package de.mhus.lib.core.io.http;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

public class InsecureHttpClientBuilder extends MHttpClientBuilder {

    private DnsResolver dnsResolver;
    private long connTimeToLive = -1;
    private TimeUnit connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
    private SocketConfig defaultSocketConfig;
    private ConnectionConfig defaultConnectionConfig;

    @Override
    protected void configureProtocolHandling(HttpClientBuilder build) {
        super.configureProtocolHandling(build);

        //        PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();
        //        DefaultHostnameVerifier hostnameVerifier = new
        // DefaultHostnameVerifier(publicSuffixMatcher);
        //        SSLConnectionSocketFactory sslSocketFactory = new
        // SSLConnectionSocketFactory(SSLContexts.createDefault(),hostnameVerifier);
        SSLConnectionSocketFactory sslSocketFactory = buildSSLSocketFactory();

        PoolingHttpClientConnectionManager poolingManager =
                new PoolingHttpClientConnectionManager(
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                                .register("https", sslSocketFactory)
                                .build(),
                        null,
                        null,
                        dnsResolver,
                        connTimeToLive,
                        connTimeToLiveTimeUnit != null
                                ? connTimeToLiveTimeUnit
                                : TimeUnit.MILLISECONDS);

        if (defaultSocketConfig != null) {
            poolingManager.setDefaultSocketConfig(defaultSocketConfig);
        }
        if (defaultConnectionConfig != null) {
            poolingManager.setDefaultConnectionConfig(defaultConnectionConfig);
        }
        //		poolingManager.setDefaultMaxPerRoute(max);
        //      poolingManager.setMaxTotal(2 * max);
        //        if (maxConnTotal > 0) {
        //            poolingManager.setMaxTotal(maxConnTotal);
        //        }
        //        if (maxConnPerRoute > 0) {
        //            poolingManager.setDefaultMaxPerRoute(maxConnPerRoute);
        //        }
        build.setConnectionManager(poolingManager);
    }

    protected SSLConnectionSocketFactory buildSSLSocketFactory() {

        HostnameVerifier hostnameVerifier =
                new HostnameVerifier() {

                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                };

        SSLConnectionSocketFactory sslSocketFactory =
                new SSLConnectionSocketFactory(SSLContexts.createDefault(), hostnameVerifier);
        return sslSocketFactory;
    }

    public DnsResolver getDnsResolver() {
        return dnsResolver;
    }

    public void setDnsResolver(DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
    }

    public long getConnTimeToLive() {
        return connTimeToLive;
    }

    public void setConnTimeToLive(long connTimeToLive) {
        this.connTimeToLive = connTimeToLive;
    }

    public TimeUnit getConnTimeToLiveTimeUnit() {
        return connTimeToLiveTimeUnit;
    }

    public void setConnTimeToLiveTimeUnit(TimeUnit connTimeToLiveTimeUnit) {
        this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
    }

    public SocketConfig getDefaultSocketConfig() {
        return defaultSocketConfig;
    }

    public void setDefaultSocketConfig(SocketConfig defaultSocketConfig) {
        this.defaultSocketConfig = defaultSocketConfig;
    }

    public ConnectionConfig getDefaultConnectionConfig() {
        return defaultConnectionConfig;
    }

    public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
        this.defaultConnectionConfig = defaultConnectionConfig;
    }
}
