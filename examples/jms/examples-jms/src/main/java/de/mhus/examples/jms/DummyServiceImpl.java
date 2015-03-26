package de.mhus.examples.jms;

import java.io.IOException;

public class DummyServiceImpl implements DummyService {

	public void dummySimple(Dummy in) {
		System.out.println("Receive: dummySimple " + in );
	}

	public Dummy dummyResult(Dummy in) {
		System.out.println("Receive: dummyResult " + in );
		return new Dummy();
	}

	public void dummyOneWay(Dummy in) {
		System.out.println("Receive: dummyOneWay " + in );
	}

	public void dummyException() throws IOException {
		throw new IOException("dummy");
	}

}
