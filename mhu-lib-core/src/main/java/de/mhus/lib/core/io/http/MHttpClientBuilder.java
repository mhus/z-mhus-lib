package de.mhus.lib.core.io.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import de.mhus.lib.core.lang.MObject;

public class MHttpClientBuilder extends MObject {

	private CloseableHttpClient hc;
	private String proxyHost;
	private int proxyPort = 3128;
	private BasicCookieStore cookieStore;
	private boolean useSystemProperties;

	/**
	 * The function will return the http client. If the http client not exists or was closed the
	 * function builds a new client.
	 * 
	 * @return
	 */
	public HttpClient getHttpClient() {
		synchronized (this) {
			if (hc == null) {
				HttpClientBuilder build = HttpClients.custom();
				configureProxy(build);
				configureCookieStore(build);
				configureProtocolHandling(build);
				hc = build.build();
			}
		}
		return hc;
	}
	
	public void close() {
		synchronized (this) {
			if (hc != null)
				try {
					hc.close();
				} catch (IOException e) {}
			hc = null;
		}
	}
	
	public boolean exists() {
		return hc != null;
	}

	protected void configureProtocolHandling(HttpClientBuilder build) {
		if (useSystemProperties)
			build.useSystemProperties();
	}
	
	protected void configureCookieStore(HttpClientBuilder build) {
		if (cookieStore == null)
			cookieStore = new BasicCookieStore();
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
}
