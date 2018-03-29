package de.mhus.lib.sql.analytics;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MTimeInterval;

public class SqlReporter extends MLog implements SqlAnalyzer {

	@Override
	public void doAnalyze(long connectionId, String original, String query, long delta, Throwable t) {
		log().i(connectionId,MTimeInterval.getIntervalAsString(delta),original,query);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

}
