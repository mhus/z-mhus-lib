package de.mhus.lib.annotations.adb;

public interface DbTransactionable {

	TransactionConnection createTransactionalConnection();

}
