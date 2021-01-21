/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.servlet.security;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.util.LongValue;

/*
<de.mhus.lib.servlet.security.SecurityApi
  maxHitsPerSecond="1000"
  maxIPsPerSecond="1000"
/>
 */
public class DefaultSecurityWatch extends MLog implements SecurityApi {

    private static CfgLong CFG_MAX_PER_SEC =
            new CfgLong(SecurityApi.class, "maxHitsPerSecond", 1000);
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
            log().t("Blocked IP", ip);
            return false;
        }
        hit.set(h + 1);

        return true;
    }
}
