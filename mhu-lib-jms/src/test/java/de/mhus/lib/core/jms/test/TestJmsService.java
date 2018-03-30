/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
