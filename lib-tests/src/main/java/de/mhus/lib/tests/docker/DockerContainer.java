package de.mhus.lib.tests.docker;

import java.util.Map;

import org.mandas.docker.client.messages.ContainerConfig;
import org.mandas.docker.client.messages.ContainerConfig.Builder;

import de.mhus.lib.core.MString;

public class DockerContainer {
	
	private String name;
	private String image;
	private String id;
	private String[] params;
    private Map<String, String> portBinding;
	
	public DockerContainer(String name, String image, String ... params) {
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

	public void setId(String id) {
		this.id = id;
	}

	public ContainerConfig buildConfig(DockerScenario scenario, DockerContainer cont) {
		Builder builder = ContainerConfig.builder()
				.hostname(name)
				.image(image);
		ContainerBuilder config = new ContainerBuilder(scenario, cont, builder);
		buildConfig(config);
        return config.build();
	}

	protected void buildConfig(ContainerBuilder config) {
		if (params == null) return;
		for (String param : params) {
			if (param.startsWith("env:") || param.startsWith("e:")) {
				config.env.add(MString.afterIndex(param, ':'));
			} else
			if (param.startsWith("vol:") || param.startsWith("v:") || param.startsWith("volume:")) {
				config.volumes.add(MString.afterIndex(param, ':'));
			} else
			if (param.startsWith("t") || param.equals("tty")) {
				config.builder.tty(true);
			} else
			if (param.startsWith("i") || param.equals("interactive")) {
				config.builder.attachStderr(true);
				config.builder.attachStdout(true);
				config.builder.attachStdin(true);
			} else 
			if (param.startsWith("link:") || param.startsWith("l:")) {
			    config.links.add(config.scenario.getPrefix() + MString.afterIndex(param, ':') );
			} else
            if (param.startsWith("port:") || param.startsWith("p:")) {
                config.ports.add(MString.afterIndex(param, ':') );
            } else
            if (param.startsWith("cmd:") ) {
                config.cmd.add(MString.afterIndex(param, ':') );
            } else
            if (param.startsWith("entrypoint:") ) {
                config.entrypoint.add(MString.afterIndex(param, ':') );
            } else
			if (buildConfigParam(config, param)) {
			} else
				System.out.println("*** Unknown param: " + param);
		}
		
	}

	protected boolean buildConfigParam(ContainerBuilder config, String param) {
		return false;
	}

    public void setPortBindings(Map<String, String> portBinding) {
        this.portBinding = portBinding;
    }
	
    public Map<String, String> getPortBindings() {
        return portBinding;
    }
    
}
