package de.mhus.lib.servlet.security;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.lang.LongValue;

/*
<de.mhus.lib.servlet.security.SecurityApi
  maxHitsPerSecond="1000"
  maxIPsPerSecond="1000"
/>
 */
public class DefaultSecurityWatch extends MLog implements SecurityApi {

	private static CfgLong CFG_MAX_PER_SEC = new CfgLong(SecurityApi.class, "maxHitsPerSecond", 1000);
	private static CfgLong CFG_MAX_IPS = new CfgLong(SecurityApi.class, "maxIPsPerSecond", 1000);
	
	private HashMap<String, LongValue> hits = new HashMap<>();
	
	long lastSec = 0;
	
	@Override
	public synchronized boolean checkHttpRequest(HttpServletRequest req, HttpServletResponse res) {
		long sec = System.currentTimeMillis() / 1000;
		
		if (lastSec != sec) {
			// cleanup
			hits.clear();
			lastSec = sec;
		}
		
		if (hits.size() > CFG_MAX_IPS.value()) {
			log().t("Blocked MAX IPS");
			return false; 
		}
		
		String ip = req.getRemoteAddr();
		LongValue hit = hits.get(ip);
		if (hit == null) {
			hit = new LongValue(0);
			hits.put(ip, hit);
		}
		long h = hit.get();
		if (h > CFG_MAX_PER_SEC.value()) {
			log().t("Blocked IP",ip);
			return false;
		}
		hit.set(h+1);
		
		return true;
	}

}
