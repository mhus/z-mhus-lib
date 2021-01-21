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
package de.mhus.lib.tests.docker;

import java.io.File;

import de.mhus.lib.core.MSystem;

public class Karaf extends DockerContainer {

    public Karaf(String name, String version, String... params) {
        super(name, "mhus/apache-karaf:" + (version == null ? "4.2.6_04" : version), params);
    }

    @Override
    protected boolean buildConfigParam(ContainerBuilder config, String param) {
        if (param.equals("debug")) {
            String m2home = System.getenv("KARAF_M2_HOME");
            if (m2home == null) m2home = MSystem.getUserHome() + File.separator + ".m2";
            config.volumes.add(m2home + ":/home/user/.m2");
            config.cmd.add("debug");
            config.ports.add("15005+:5005");
            return true;
        }
        return false;
    }

    @Override
    protected void buildConfig(ContainerBuilder config) {

        super.buildConfig(config);

        config.builder.withTty(true);
        config.builder.withStdinOpen(true);
        config.builder.withAttachStdout(true);
        config.builder.withAttachStderr(true);
    }
}
