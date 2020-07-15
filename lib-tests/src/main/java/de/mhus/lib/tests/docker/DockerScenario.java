package de.mhus.lib.tests.docker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.mandas.docker.client.DockerClient;
import org.mandas.docker.client.DockerClient.AttachParameter;
import org.mandas.docker.client.DockerClient.ListContainersParam;
import org.mandas.docker.client.DockerClient.LogsParam;
import org.mandas.docker.client.LogMessage;
import org.mandas.docker.client.LogStream;
import org.mandas.docker.client.ProgressHandler;
import org.mandas.docker.client.builder.jersey.JerseyDockerClientBuilder;
import org.mandas.docker.client.exceptions.DockerCertificateException;
import org.mandas.docker.client.exceptions.DockerException;
import org.mandas.docker.client.messages.Container;
import org.mandas.docker.client.messages.ContainerCreation;
import org.mandas.docker.client.messages.ProgressMessage;

import de.mhus.lib.errors.NotFoundException;

// https://github.com/spotify/docker-client
public class DockerScenario {

	private LinkedList<DockerContainer> containers = new LinkedList<>();
	private DockerClient docker;
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
		    if (System.getenv("DOCKER_HOST") == null) {
    		    System.clearProperty("http.proxyHost");
                System.clearProperty("https.proxyHost");
                System.clearProperty("ftp.proxyHost");
		    }
		    docker = new JerseyDockerClientBuilder().fromEnv().build(); // For Jersey
//			docker = new DefaultDockerClient("unix:///var/run/docker.sock");
		}
	}
	
	public void start() throws DockerCertificateException, DockerException, InterruptedException {
		init();
		destroy();
		for (DockerContainer cont : containers) {
			System.out.println("--- Create " + cont.getName());
			
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
			ContainerCreation creation = docker.createContainer(cont.buildConfig(this, cont), prefix + cont.getName());
			cont.setId(creation.id());
			for (String warnings : creation.warnings())
				System.out.println("    " + warnings);
		}
        for (DockerContainer cont : containers) {
            System.out.println("--- Start " + cont.getName());
            docker.startContainer(cont.getId());
        }
	}
	
	public void destroy() throws DockerCertificateException, DockerException, InterruptedException {
		init();
		fetchContainers();
		
        ArrayList<DockerContainer> reverse = new ArrayList<>(containers);
        Collections.reverse(reverse);
		for (DockerContainer cont : reverse) {
			if (cont.getId() != null) {
				System.out.println("--- Stop " + cont.getName());
				docker.stopContainer(cont.getId(), 15);
			}
		}

		for (DockerContainer cont : reverse) {
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
		return docker.attachContainer(cont.getId(), 
		        AttachParameter.STDERR, 
		        AttachParameter.STDOUT, 
		        AttachParameter.STDIN,
		        AttachParameter.STREAM);
	}
	
	public LogStream logs(String name, boolean follow, int tail) throws NotFoundException, DockerException, InterruptedException {
		DockerContainer cont = get(name);
		if (cont.getId() == null) throw new NotFoundException("Container not started",cont.getName());
		if (tail > 0) 
	        return docker.logs(cont.getId(), 
	                LogsParam.stdout(), 
	                LogsParam.stderr(), 
	                LogsParam.follow(follow),
	                LogsParam.tail(tail));
		else
    	    return docker.logs(cont.getId(), 
    	            LogsParam.stdout(), 
    	            LogsParam.stderr(), 
    	            LogsParam.follow(follow));
	}
	
	public void waitForLogEntry(String name, String waitForString, int tail, boolean print) throws NotFoundException, DockerException, InterruptedException, IOException {
        try (LogStream logStream = logs("karaf", true, tail)) {
            while (logStream.hasNext()) {
                LogMessage msg = logStream.next();
                String logStr = StandardCharsets.UTF_8.decode(msg.content()).toString();
                if (print)
                    System.out.println(logStr);
                if (logStr.contains(waitForString)) {
                    return;
                }
            }
        }
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
