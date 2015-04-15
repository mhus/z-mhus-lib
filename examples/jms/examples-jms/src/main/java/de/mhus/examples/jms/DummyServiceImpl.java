package de.mhus.examples.jms;

import java.io.IOException;

public class DummyServiceImpl implements DummyService {

	@Override
	public void dummySimple(Dummy in) {
		System.out.println("Receive: dummySimple " + in );
	}

	@Override
	public Dummy dummyResult(Dummy in) {
		System.out.println("Receive: dummyResult " + in );
		return new Dummy();
	}

	@Override
	public void dummyOneWay(Dummy in) {
		System.out.println("Receive: dummyOneWay " + in );
	}

	@Override
	public void dummyException() throws IOException {
		throw new IOException("dummy");
	}

}
