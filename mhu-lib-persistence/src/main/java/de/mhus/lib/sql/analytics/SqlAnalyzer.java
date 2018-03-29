package de.mhus.lib.sql.analytics;

public interface SqlAnalyzer {

	void doAnalyze(long connectionId, String original, String query, long delta, Throwable t);

	void start();
	
	void stop();
	
}
