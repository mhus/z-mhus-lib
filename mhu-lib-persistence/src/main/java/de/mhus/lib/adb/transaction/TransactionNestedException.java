package de.mhus.lib.adb.transaction;

public class TransactionNestedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TransactionNestedException(String msg) {
		super(msg);
	}

}
