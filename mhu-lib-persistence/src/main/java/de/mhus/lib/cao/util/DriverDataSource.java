package de.mhus.lib.cao.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDataSource;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.core.MSystem;

public class DriverDataSource implements CaoDataSource {

	private CaoDriver driver;
	private String uri;
	private String credentials;
	private String type;
	private String name;
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public CaoConnection getConnection() throws Exception {
		return driver.connect(uri, credentials);
	}

	public CaoDriver getDriver() {
		return driver;
	}

	public void setDriver(CaoDriver driver) {
		this.driver = driver;
		this.type = driver.getClass().getCanonicalName();
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return MSystem.toString(this, driver, uri );
	}
	
}
