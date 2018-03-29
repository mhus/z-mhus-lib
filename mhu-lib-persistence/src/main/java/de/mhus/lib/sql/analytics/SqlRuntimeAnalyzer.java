package de.mhus.lib.sql.analytics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.logging.MLogUtil;

public class SqlRuntimeAnalyzer extends SqlRuntimeWarning {

	protected HashMap<String, Container> list = new HashMap<>();

	@Override
	public void doAnalyze(long connectionId, String original, String query, long delta, Throwable t) {
		super.doAnalyze(connectionId, original, query, delta, t);
		if (t != null) return;
		
		synchronized (this) {
			Container container = list.get(original);
			if (container == null) {
				container = new Container(original);
				list.put(original, container);
			}
			container.add(delta);
		}
		
	}
	
	public Collection<Container> getData() {
		synchronized (this) {
			return Collections.unmodifiableCollection(list.values());
		}
	}
	
	public static class Container {
		
		private String sql;
		private int cnt;
		private long runtime;

		public Container(String original) {
			sql = original;
		}

		public void add(long delta) {
			cnt++;
			runtime+=delta;
		}

		public String getSql() {
			return sql;
		}

		public int getCnt() {
			return cnt;
		}

		public long getRuntime() {
			return runtime;
		}
		
	}

}
