package de.mhus.lib.annotations.adb;

public interface TransactionConnection {

	void commit() throws Exception;

	void rollback() throws Exception;

}
