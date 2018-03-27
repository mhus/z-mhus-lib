package de.mhus.lib.sql.analytics;

public interface SqlAnalyzer {

	void doAnalyze(long connectionId, String original, String query, long delta);

	void start();
	
	void stop();
	
}
