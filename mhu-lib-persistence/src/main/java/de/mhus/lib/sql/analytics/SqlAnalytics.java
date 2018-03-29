package de.mhus.lib.sql.analytics;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogUtil;

public class SqlAnalytics {

	private static Log log = Log.getLog(SqlAnalytics.class);
	private static SqlAnalyzer analyzer = null;
	
	public static void setAnalyzer(SqlAnalyzer analyzer_) {
		try {
			if (analyzer != null) analyzer.stop();
			analyzer = analyzer_;
			if (analyzer != null) analyzer.start();
		} catch (Throwable t) {
			log.e(t);
			analyzer = null;
		}
	}

	public static SqlAnalyzer getAnalyzer() {
		return analyzer;
	}

	public static void trace(long connectionId, String original, String query, long start, Throwable t) {
		try {
			long delta = System.currentTimeMillis() - start;
			if (analyzer != null)
				analyzer.doAnalyze(connectionId,original,query,delta, t);
		} catch (Throwable t2) {
			log.e(t2);
		}
	}

}
