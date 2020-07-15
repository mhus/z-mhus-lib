package de.mhus.lib.tests.docker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mandas.docker.client.messages.HostConfig;
import org.mandas.docker.client.messages.PortBinding;
import org.mandas.docker.client.messages.ContainerConfig;
import org.mandas.docker.client.messages.ContainerConfig.Builder;

public class ContainerBuilder {
    
    final public DockerScenario scenario;
    final public Builder builder;
    final public org.mandas.docker.client.messages.HostConfig.Builder hostBuilder;
    final public DockerContainer cont;
    
    final public LinkedList<String> links = new LinkedList<>();
    final public LinkedList<String> cmd = new LinkedList<>();
    final public LinkedList<String> env = new LinkedList<>();
    final public LinkedList<String> entrypoint = new LinkedList<>();
    final public LinkedList<String> volumes = new LinkedList<>();
    final public LinkedList<String> ports = new LinkedList<>();
    
    public ContainerBuilder(DockerScenario scenario, DockerContainer cont, Builder builder) {
        this.scenario = scenario;
        this.builder = builder;
        this.cont = cont;
        this.hostBuilder = HostConfig.builder();
    }

    public ContainerConfig build() {

        if (ports.size() > 0) {
            Map<String, List<PortBinding>> portBindings = new HashMap<>();
            Map<String, String> portBindingInfo = new HashMap<>();
            for (String port : ports) {
                List<PortBinding> randomPort = new ArrayList<>();
                PortBinding random = PortBinding.randomPort("0.0.0.0");
                randomPort.add(random);
                portBindings.put(port, randomPort);
                portBindingInfo.put(port, random.hostPort());
            }
            hostBuilder.portBindings(portBindings);
            cont.setPortBindings(portBindingInfo);
        } else {
            cont.setPortBindings(new HashMap<>());
        }
        
        if (entrypoint.size() > 0)
            builder.entrypoint(entrypoint);
        
        if (volumes.size() > 0)
            builder.addVolumes(volumes.toArray(new String[0]));
        
        if (env.size() > 0)
            builder.env(env);
        
        if (cmd.size() > 0)
            builder.cmd(cmd);

        if (links.size() > 0)
            hostBuilder.links(links);

        builder.hostConfig(hostBuilder.build());
        
        return builder.build();
    }

}
