package de.mhus.examples.jms;

import java.io.IOException;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.jms.ClientJsonObject;
import de.mhus.lib.jms.RequestResult;

public class DummyServiceSender implements DummyService {

	private ClientJsonObject client;

	public DummyServiceSender(ClientJsonObject client) {
		this.client = client;
	}
	@Override
	public void dummySimple(Dummy in) {
		MProperties prop = new MProperties("function","dummySimple");
		try {
			client.sendObject(prop, in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Dummy dummyResult(Dummy in) {
		MProperties prop = new MProperties("function","dummyResult");
		try {
			RequestResult<Object> res = client.sendObject(prop, in);
			// check success and throw exceptions
			if (res == null) return null; // exception ?
			return (Dummy) res.getResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // throw a runtime ex
	}

	@Override
	public void dummyOneWay(Dummy in) {
		MProperties prop = new MProperties("function","dummyOneWay");
		try {
			client.sendObjectOneWay(prop, in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void dummyException() throws IOException {
		
	}

}
