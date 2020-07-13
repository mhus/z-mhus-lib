package de.mhus.lib.tests.docker;

import java.util.LinkedList;

import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerConfig.Builder;
import com.spotify.docker.client.messages.HostConfig;

public class DockerContainer {
	
	private String name;
	private String image;
	private String id;
	private String[] params;
	
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

	public ContainerConfig buildConfig(DockerScenario scenario) {
		Builder config = ContainerConfig.builder()
				.hostname(name)
				.image(image);
		buildConfig(scenario, config);
		return config.build();
	}

	protected void buildConfig(DockerScenario scenario, Builder config) {
		if (params == null) return;
		LinkedList<String> links = new LinkedList<>();
		for (String param : params) {
			if (param.startsWith("env:")) {
				config.env(param.substring(4));
			} else
			if (param.startsWith("vol:")) {
				config.addVolume(param.substring(4));
			} else
			if (param.startsWith("t")) {
				config.tty(true);
			} else
			if (param.startsWith("i")) {
				config.attachStderr(true);
				config.attachStdout(true);
				config.attachStdin(true);
			} else 
			if (param.startsWith("link:")) {
				links.add(scenario.getPrefix() + param.substring(5));
			} else
			if (buildConfig(scenario, param, config)) {
			} else
				System.out.println("*** Unknown param: " + param);
		}
		
		if (links.size() > 0) {
			HostConfig hostConfig = HostConfig.builder()
					.links(links)
					.build();
			config.hostConfig(hostConfig);
		}
		
	}

	protected boolean buildConfig(DockerScenario scenario, String param, Builder config) {
		return false;
	}
	
}
