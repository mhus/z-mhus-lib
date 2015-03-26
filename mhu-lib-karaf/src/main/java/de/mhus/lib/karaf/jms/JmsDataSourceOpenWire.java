package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;

import de.mhus.lib.jms.JmsConnection;

public class JmsDataSourceOpenWire implements JmsDataSource {

	private String name;
	private String url;
	private String user;
	private String password;
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public JmsConnection createConnection() throws JMSException {
		return new JmsConnection(url, user, password);
	}

}
