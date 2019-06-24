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
package de.mhus.lib.core.service;

import java.io.File;
import java.util.UUID;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.errors.MException;

public class ServerIdent extends MObject {

	private CfgString CFG_IDENT = new CfgString(ServerIdent.class, "ident", null ) {
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

    public static MProperties ATTRIBUTES = new MProperties();
    static {
        ATTRIBUTES.setString("random", String.valueOf(MMath.toBasis36((long)(Math.random()*36*36*36*36), 4)) );
        ATTRIBUTES.setString("time",   String.valueOf(MMath.toBasis36(System.currentTimeMillis(), 4)) );
        ATTRIBUTES.setString("uuid",   UUID.randomUUID().toString() );
    }
    private String ident = null;
    
	public ServerIdent() {
	    update();
	}
	
    private synchronized void update() {

        ident = CFG_IDENT.value();

	    if (ident == null) {
    		String persistence = MApi.getCfg(ServerIdent.class).getString("persistence", MApi.getFile(MApi.SCOPE.ETC, ServerIdent.class.getCanonicalName() + ".properties").getAbsolutePath() );
    		File file = new File(persistence);
    		String def = null;
    		if (file.exists() && file.isFile()) {
    			ATTRIBUTES = MProperties.load(file);
    			def = ATTRIBUTES.getString("default", null);
    		}
    		if (def == null) {
    			try {
                    def = create();
                    ATTRIBUTES.setString("default", def);
                    ATTRIBUTES.save(file);
                } catch (Exception e) {
                    log().e(e);
                }
    		}
    		def = MFile.normalize(def);
    		ident = def;
	    }
	}
	
	private String create() throws MException {
        String created = StringCompiler.compile(CFG_PATTERN.value()).execute(ATTRIBUTES);
        return created;
    }

    @Override
	public String toString() {
		return ident;
	}
}
