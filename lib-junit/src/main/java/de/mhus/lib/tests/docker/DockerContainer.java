/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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

import java.util.ArrayList;
import java.util.Map;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports.Binding;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MString;

public class DockerContainer {

    private String name;
    private String image;
    private String id;
    private String[] params;
    private DockerScenario scenario;

    public DockerContainer(String name, String image, String... params) {
        this.name = name;
        this.image = image;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public void setId(DockerScenario scenario, String id) {
        this.scenario = scenario;
        this.id = id;
    }

    public void buildConfig(DockerScenario scenario, CreateContainerCmd builder) {

        builder.withHostName(name);
        builder.withImage(image);
        ContainerBuilder config = new ContainerBuilder(scenario, this, builder);
        buildConfig(config);
        config.build();
    }

    protected void buildConfig(ContainerBuilder config) {
        if (params == null) return;
        
//        ArrayList<Integer> size = new ArrayList<>();
//        size.add(240);
//        size.add(240);
//        config.hostBuilder.withConsoleSize( size );
        
        for (String param : params) {
            if (param.equals("privileged")) {
                config.hostBuilder.withPrivileged(true);
            } else if (param.startsWith("user:")) {
                config.builder.withUser(MString.afterIndex(param, ':'));
            } else if (param.startsWith("env:") || param.startsWith("e:")) {
                config.env.add(MString.afterIndex(param, ':'));
            } else if (param.startsWith("vol:")
                    || param.startsWith("v:")
                    || param.startsWith("volume:")) {
                config.volumes.add(MString.afterIndex(param, ':'));
            } else if (param.startsWith("t") || param.equals("tty")) {
                config.builder.withTty(true);
            } else if (param.startsWith("i") || param.equals("interactive")) {
                config.builder.withStdinOpen(true);
                config.builder.withAttachStdout(true);
                config.builder.withAttachStderr(true);
            } else if (param.startsWith("link:") || param.startsWith("l:")) {
                config.links.add(config.scenario.getPrefix() + MString.afterIndex(param, ':'));
            } else if (param.startsWith("port:") || param.startsWith("p:")) {
                config.ports.add(MString.afterIndex(param, ':'));
            } else if (param.startsWith("cmd:")) {
                config.cmd.add(MString.afterIndex(param, ':'));
            } else if (param.startsWith("entrypoint:")) {
                config.entrypoint.add(MString.afterIndex(param, ':'));
            } else if (buildConfigParam(config, param)) {
            } else System.out.println("*** Unknown param: " + param);
        }
    }

    protected boolean buildConfigParam(ContainerBuilder config, String param) {
        return false;
    }

    public boolean isRunning() {
        if (scenario == null || id == null) return false;
        try {
            InspectContainerResponse resp =
                    scenario.getClient().inspectContainerCmd(getId()).exec();
            return resp.getState().getRunning();
        } catch (com.github.dockerjava.api.exception.NotFoundException e) {
            e.printStackTrace();
            id = null;
            return false;
        }
    }

    @SuppressWarnings("resource")
    public int getPortBinding(int exposed) {
        for (Map.Entry<ExposedPort, Binding[]> binding :
                scenario.getClient()
                        .inspectContainerCmd(id)
                        .exec()
                        .getNetworkSettings()
                        .getPorts()
                        .getBindings()
                        .entrySet()) {
            if (binding.getKey().getPort() == exposed) {
                if (binding.getValue().length > 0)
                    return M.to(binding.getValue()[0].getHostPortSpec(), 0);
            }
        }
        return 0;
    }

    public String getExternalHost() {
        return scenario.getExternalHost();
    }
}
