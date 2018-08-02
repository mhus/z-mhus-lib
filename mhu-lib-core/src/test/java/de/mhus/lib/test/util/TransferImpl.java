package de.mhus.lib.test.util;

public class TransferImpl implements TransferIfc {

	@Override
	public int hello() {
		System.out.println("Hello World");
		return 1;
	}

}
