package de.mhus.lib.jms.ping;

import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MSystem;

public class PingImpl implements Ping {

	@Override
	public long time() {
		return System.currentTimeMillis();
	}

	@Override
	public List<String> ping(byte[] b) {
		LinkedList<String> out = new LinkedList<>();
		out.add(hostname());
		return out;
	}

	@Override
	public long timeDiff(long time) {
		return System.currentTimeMillis() - time;
	}

	@Override
	public String hostname() {
		return MSystem.getHostname();
	}

}
