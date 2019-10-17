/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.base.service;

import java.io.File;
import java.util.UUID;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.parser.StringCompiler;

public class ServerIdent extends MObject {

	private CfgString CFG_IDENT = new CfgString(ServerIdent.class, "ident", null ) {
	    @Override
        protected void onPostUpdate(String newValue) {
	        super.onPostUpdate(newValue);
	       update();
	    }
	};
    private CfgString CFG_SERVICE = new CfgString(ServerIdent.class, "service", null ) {
        @Override
        protected void onPostUpdate(String newValue) {
            super.onPostUpdate(newValue);
           update();
        }
    };
	
    private CfgString CFG_PATTERN = new CfgString(ServerIdent.class, "pattern", "$#hostname$-$time$$random$" ) {
        @Override
        protected void onPostUpdate(String newValue) {
            super.onPostUpdate(newValue);
           update();
        }
    };
    
    private CfgString CFG_SERVICE_PATTERN = new CfgString(ServerIdent.class, "patternService", "$#hostname$" ) {
        @Override
        protected void onPostUpdate(String newValue) {
            super.onPostUpdate(newValue);
           update();
        }
    };
    
    private CfgBoolean CFG_PERSISTENT = new CfgBoolean(ServerIdent.class, "persistent", true);

    private static MProperties ATTRIBUTES = new MProperties();
    static {
        ATTRIBUTES.setString("random", String.valueOf(MMath.toBasis36((long)(Math.random()*36*36*36*36), 4)) );
        ATTRIBUTES.setString("time",   String.valueOf(MMath.toBasis36(System.currentTimeMillis(), 4)) );
        ATTRIBUTES.setString("uuid",   UUID.randomUUID().toString() );
    }
    private String ident = null;
    private String service = null;
    
	public ServerIdent() {
	    update();
	}
	
	public static MProperties getAttributes() {
	    return ATTRIBUTES;
	}
	
    private synchronized void update() {

        if (CFG_IDENT == null || CFG_PATTERN == null || CFG_PERSISTENT == null || CFG_SERVICE == null || CFG_SERVICE_PATTERN == null) return; // do not update in init time

        ident = CFG_IDENT.value();

	    if (ident == null) {
    		String persistence = MApi.getCfg(ServerIdent.class).getString("ident", MApi.getFile(MApi.SCOPE.ETC, ServerIdent.class.getCanonicalName() + ".properties").getAbsolutePath() );
    		File file = new File(persistence);
    		String def = null;
    		if (CFG_PERSISTENT.value() && file.exists() && file.isFile()) {
    			ATTRIBUTES = MProperties.load(file);
    			def = ATTRIBUTES.getString("default", null);
    		}
    		if (def == null) {
    			try {
    			    def = StringCompiler.compile(CFG_PATTERN.value()).execute(ATTRIBUTES);
                    ATTRIBUTES.setString("default", def);
                    if (CFG_PERSISTENT.value())
                        ATTRIBUTES.save(file);
                } catch (Exception e) {
                    log().e(e);
                }
    		}
    		def = MFile.normalize(def);
    		ident = def;
	    }
	    
	    service = CFG_SERVICE.value();
	    
	    if (service == null) {
            String persistence = MApi.getCfg(ServerIdent.class).getString("service", MApi.getFile(MApi.SCOPE.ETC, ServerIdent.class.getCanonicalName() + ".properties").getAbsolutePath() );
            File file = new File(persistence);
            String def = null;
            if (CFG_PERSISTENT.value() && file.exists() && file.isFile()) {
                ATTRIBUTES = MProperties.load(file);
                def = ATTRIBUTES.getString("default", null);
            }
            if (def == null) {
                try {
                    def = StringCompiler.compile(CFG_SERVICE_PATTERN.value()).execute(ATTRIBUTES);
                    ATTRIBUTES.setString("default", def);
                    if (CFG_PERSISTENT.value())
                        ATTRIBUTES.save(file);
                } catch (Exception e) {
                    log().e(e);
                }
            }
            def = MFile.normalize(def);
            ident = def;
	    }
	}
	
    @Override
	public String toString() {
		return ident + "@" + service;
	}
    
    public String getIdent() {
        return ident;
    }
    
    public String getService() {
        return service;
    }
    
}
