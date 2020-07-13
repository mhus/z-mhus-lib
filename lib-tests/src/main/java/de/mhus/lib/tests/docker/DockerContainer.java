package de.mhus.lib.tests.docker;

import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerConfig.Builder;

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

	public ContainerConfig buildConfig() {
		Builder config = ContainerConfig.builder()
				.hostname(name)
				.image(image);
		buildConfig(config);
		return config.build();
	}

	protected void buildConfig(Builder config) {
		if (params == null) return;
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
			if (buildConfig(param, config)) {
			} else
				System.out.println("*** Unknown param: " + param);
		}
	}

	protected boolean buildConfig(String param, Builder config) {
		return false;
	}
	
}
