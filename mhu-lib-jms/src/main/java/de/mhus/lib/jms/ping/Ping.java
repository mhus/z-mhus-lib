package de.mhus.lib.jms.ping;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Ping {

	@WebMethod
	long time();
	
	@WebMethod
	List<String> ping(byte[] b);

	@WebMethod
	long timeDiff(long time);

	@WebMethod
	String hostname();

}
