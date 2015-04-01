package de.mhus.lib.core.jms.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface TestJmsService {

	@WebMethod
	void withoutReturnValue();
	
	@WebMethod
	@Oneway
	void oneWayWithoutReturn();
	
	@WebMethod
	void withParameters(String nr1, long nr2, int nr3);
	
	@WebMethod
	String withParametersAndReturn(String nr1, long nr2, int nr3);
	
	@WebMethod
	Map<String,String> mapSample(Map<String,String> in);
	
	@WebMethod
	List<String> listSample(List<String> in);
	
	@WebMethod
	void receiveMessage(Message raw) throws JMSException;
	
	@WebMethod
	Message sendMessage(String text) throws JMSException;

	@WebMethod
	List<Message> messageBroadcast(String text) throws JMSException;

	@WebMethod
	void throwException(String text) throws IOException;
}
