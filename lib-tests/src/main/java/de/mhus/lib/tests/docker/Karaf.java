package de.mhus.lib.tests.docker;

import java.io.File;

import de.mhus.lib.core.MSystem;

public class Karaf extends DockerContainer {

    public Karaf(String name, String version, String ... params) {
        super(name, "mhus/karaf-k8s:" + (version == null ? "last" : version), params);
    }

    @Override
    protected boolean buildConfigParam(ContainerBuilder config, String param) {
        if (param.equals("debug")) {
            config.volumes.add(MSystem.getUserHome() + File.separator + ".m2:/home/user/.m2");
            config.cmd.add("debug");
            config.ports.add("5005");
            return true;
        }
        return false;
    }

    @Override
    protected void buildConfig(ContainerBuilder config) {
        config.builder.tty(true);
        config.builder.attachStderr(true);
        config.builder.attachStdin(true);
        config.builder.attachStdout(true);

        super.buildConfig(config);
    }

}
