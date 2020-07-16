package de.mhus.lib.tests.docker;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;

import de.mhus.lib.core.MString;

public class DockerContainer {
	
	private String name;
	private String image;
	private String id;
	private String[] params;
    private DockerScenario scenario;
	
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
		for (String param : params) {
			if (param.startsWith("env:") || param.startsWith("e:")) {
				config.env.add(MString.afterIndex(param, ':'));
			} else
			if (param.startsWith("vol:") || param.startsWith("v:") || param.startsWith("volume:")) {
				config.volumes.add(MString.afterIndex(param, ':'));
			} else
			if (param.startsWith("t") || param.equals("tty")) {
				config.builder.withTty(true);
			} else
			if (param.startsWith("i") || param.equals("interactive")) {
				config.builder.withStdinOpen(true);
				config.builder.withAttachStdout(true);
				config.builder.withAttachStderr(true);
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

    public boolean isRunning() {
        if (scenario == null || id == null) return false;
        try {
            InspectContainerResponse resp = scenario.getClient().inspectContainerCmd(getId()).exec();
            return resp.getState().getRunning();
        } catch (com.github.dockerjava.api.exception.NotFoundException e) {
            e.printStackTrace();
            id = null;
            return false;
        }
    }

}
