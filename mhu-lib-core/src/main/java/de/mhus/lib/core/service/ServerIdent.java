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

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.cfg.CfgString;

public class ServerIdent {

	private CfgString ident;
	
	public ServerIdent() {
		String persistence = MApi.getCfg(ServerIdent.class).getString("persistence", MApi.getFile(".ident.txt").getAbsolutePath() );
		File file = new File(persistence);
		String def = "";
		if (file.exists() && file.isFile())
			def = MFile.readFile(file).trim();
		else {
			def = MSystem.getHostname() + "." + String.valueOf(MMath.toBasis36((long)(Math.random()*36*36*36*36), 4 ));
			MFile.writeFile(file, def);
		}
		def = MFile.normalize(def);
		ident = new CfgString(ServerIdent.class, "ident", def );
	}
	
	public String toString() {
		return ident.value();
	}
}
