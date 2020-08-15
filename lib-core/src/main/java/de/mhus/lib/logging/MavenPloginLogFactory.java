package de.mhus.lib.logging;

import org.apache.maven.plugin.AbstractMojo;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

public class MavenPloginLogFactory extends LogFactory {

    private AbstractMojo owner;

    public MavenPloginLogFactory(AbstractMojo owner) {
        this.owner = owner;
    }

    @Override
    public void init(IConfig config) throws Exception {}

    @Override
    public LogEngine createInstance(String name) {
        return new MavenPluginLogEngine(owner, name);
    }
}
