package de.mhus.lib.core.service;

import java.io.File;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.cfg.CfgString;

/**
 * <p>ServerIdent class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class ServerIdent {

	private CfgString ident;
	
	/**
	 * <p>Constructor for ServerIdent.</p>
	 */
	public ServerIdent() {
		String persistence = MSingleton.getCfg(ServerIdent.class).getString("persistence", MSingleton.getFile(".ident.txt").getAbsolutePath() );
		File file = new File(persistence);
		String def = "";
		if (file.exists() && file.isFile())
			def = MFile.readFile(file).trim();
		else {
			def = String.valueOf(MMath.toBasis36((long)(Math.random()*36*36*36*36), 4 ));
			MFile.writeFile(file, def);
		}
			ident = new CfgString(ServerIdent.class, "ident", def );
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return ident.value();
	}
}
