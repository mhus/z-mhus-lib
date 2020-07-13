package de.mhus.lib.tests.docker;

import java.util.LinkedList;

import com.google.common.collect.Lists;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.DockerClient.LogsParam;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ProgressMessage;

import de.mhus.lib.errors.NotFoundException;

// https://github.com/spotify/docker-client
public class DockerScenario {

	private LinkedList<DockerContainer> containers = new LinkedList<>();
	private DefaultDockerClient docker;
	private String prefix = "test-";

	public DockerScenario() {}
	
	public DockerScenario(String prefix) {
		this.prefix = prefix;
	}
	
	public DockerScenario add(String name, String image, String ... params) {
		return add(new DockerContainer(name, image, params));
	}
	
	public DockerScenario add(DockerContainer inst) {
		containers.add(inst);
		return this;
	}
	
	public DockerContainer get(String name) throws NotFoundException {
		for (DockerContainer inst : containers)
			if (inst.getName().equals(name))
				return inst;
		throw new NotFoundException("container not found", name);
	}
	
	public void init() throws DockerCertificateException {
		if (docker == null) {
			docker = DefaultDockerClient.fromEnv().build();
//			docker = new DefaultDockerClient("unix:///var/run/docker.sock");
		}
	}
	
	public void start() throws DockerCertificateException, DockerException, InterruptedException {
		init();
		destroy();
		for (DockerContainer cont : Lists.reverse(containers)) {
			System.out.println("--- Start " + cont.getName());
			
			try {
				docker.inspectImage(cont.getImage());
			} catch (DockerException e) {
				System.out.println("    Load: " + cont.getImage());
				docker.pull(cont.getImage(), new ProgressHandler() {
					
					@Override
					public void progress(ProgressMessage message) throws DockerException {
						System.out.println("    " + message.progress());
					}
				});
			}
			ContainerCreation creation = docker.createContainer(cont.buildConfig(this), prefix + cont.getName());
			cont.setId(creation.id());
			for (String warnings : creation.warnings())
				System.out.println("    " + warnings);
			docker.startContainer(cont.getId());
		}
	}
	
	public void destroy() throws DockerCertificateException, DockerException, InterruptedException {
		init();
		fetchContainers();
		
		for (DockerContainer cont : Lists.reverse(containers)) {
			if (cont.getId() != null) {
				System.out.println("--- Stop " + cont.getName());
				docker.stopContainer(cont.getId(), 15);
			}
		}

		for (DockerContainer cont : Lists.reverse(containers)) {
			if (cont.getId() != null) {
				System.out.println("--- Remove " + cont.getName());
				docker.removeContainer(cont.getId());
				cont.setId(null);
			}
		}
		
	}
	
	public LogStream attach(String name) throws NotFoundException, DockerException, InterruptedException {
		DockerContainer cont = get(name);
		if (cont.getId() == null) throw new NotFoundException("Container not started",cont.getName());
		return docker.attachContainer(cont.getId());
	}
	
	public LogStream logs(String name) throws NotFoundException, DockerException, InterruptedException {
		DockerContainer cont = get(name);
		if (cont.getId() == null) throw new NotFoundException("Container not started",cont.getName());
		return docker.logs(cont.getId(), LogsParam.stdout(), LogsParam.stderr());
	}

	private void fetchContainers() throws DockerException, InterruptedException {
		
		for (DockerContainer cont2 : containers)
			cont2.setId(null);
		
		for (Container cont : docker.listContainers(ListContainersParam.allContainers())) {
			System.out.println("Existing " + cont.names());
			for (DockerContainer cont2 : containers) {
				if (cont.names().contains("/" + prefix + cont2.getName()))
					cont2.setId(cont.id());
			}
		}
		
	}

	public String getPrefix() {
		return prefix;
	}
	
}
