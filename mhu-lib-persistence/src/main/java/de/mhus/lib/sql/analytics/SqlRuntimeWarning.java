package de.mhus.lib.sql.analytics;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.system.CfgManager;
import de.mhus.lib.core.system.IApiInternal;

public class SqlRuntimeWarning extends MLog implements SqlAnalyzer, CfgInitiator {

	private long traceMaxRuntime = MTimeInterval.MINUTE_IN_MILLISECOUNDS;

	@Override
	public void doAnalyze(long connectionId, String original, String query, long delta) {
		if (delta > traceMaxRuntime) {
			log().f(connectionId,"Query Runtime Warning",delta,query);
			MLogUtil.logStackTrace(log(), ""+connectionId, Thread.currentThread().getStackTrace());
		}
	}

	public long getTraceMaxRuntime() {
		return traceMaxRuntime;
	}

	public void setTraceMaxRuntime(long traceMaxRuntime) {
		this.traceMaxRuntime = traceMaxRuntime;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void doInitialize(IApiInternal internal, CfgManager manager, IConfig config) {
		if (config != null)
			traceMaxRuntime = config.getLong("traceMaxRuntime", traceMaxRuntime);
		SqlAnalytics.setAnalyzer(this);
	}

}
