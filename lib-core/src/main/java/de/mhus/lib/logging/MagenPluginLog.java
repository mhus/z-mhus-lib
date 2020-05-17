package de.mhus.lib.logging;

import org.apache.maven.plugin.AbstractMojo;

import de.mhus.lib.core.logging.Log;

public class MagenPluginLog extends Log {

	public MagenPluginLog(AbstractMojo owner) {
		super(owner);
		engine = new MavenPluginLogEngine(owner);
	}

}
