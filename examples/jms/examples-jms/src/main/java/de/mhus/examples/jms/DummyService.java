package de.mhus.examples.jms;

import java.io.IOException;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface DummyService {

	@WebMethod
	void dummySimple(Dummy in);
	
	@WebMethod
	Dummy dummyResult(Dummy in);
	
	@WebMethod
	@Oneway
	void dummyOneWay(Dummy in);

	@WebMethod
	void dummyException() throws IOException;
	
}
