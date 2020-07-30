package de.mhus.lib.tests.docker;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MThread;
import de.mhus.lib.errors.NotFoundException;

// https://github.com/docker-java/docker-java
// https://github.com/docker-java/docker-java/blob/77103e319060e8fa3a4d0caaf2773597a907687c/docker-java/src/test/java/com/github/dockerjava/cmd/StartContainerCmdIT.java

public class DockerScenario {

	private LinkedList<DockerContainer> containers = new LinkedList<>();
	private DockerClient docker;
	private String prefix = "test-";
	private Timer watch = new Timer("DockerScenarioTimer", true);
    private String networkId;
    private boolean useExistingNetwork = true;

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
	
	public void init() {
		if (docker == null) {
		    if (System.getenv("DOCKER_HOST") == null) {
    		    System.clearProperty("http.proxyHost");
                System.clearProperty("https.proxyHost");
                System.clearProperty("ftp.proxyHost");
		    }
		    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
		    DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
		            .dockerHost(config.getDockerHost())
		            .sslConfig(config.getSSLConfig())
		            .build();
		    
		    docker = DockerClientImpl.getInstance(config, httpClient);
		}
	}
	
	public void start() throws InterruptedException {
		init();
		destroy();
        destroyNetwork(false);
		
		// create network
		createNetwork();
		
		// create containers
		
		for (DockerContainer cont : containers) {
			System.out.println("--- Create " + cont.getName());
			
			try {
				docker.inspectImageCmd(cont.getImage()).exec();
			} catch (com.github.dockerjava.api.exception.NotFoundException e) {
				System.out.println("    Load: " + cont.getImage());
				docker.pullImageCmd(cont.getImage())
				    .start().awaitCompletion();
			}

			CreateContainerCmd containerCmd = docker.createContainerCmd(cont.getImage());

			cont.buildConfig(this, containerCmd);
			containerCmd.withName(prefix + cont.getName());
			CreateContainerResponse resp = containerCmd.exec();
			cont.setId(this, resp.getId());
			
			for (String warning : resp.getWarnings())
				System.out.println("    " + warning);
		}
		
        for (DockerContainer cont : containers) {
            docker.connectToNetworkCmd()
                .withContainerId(cont.getId())
                .withNetworkId(networkId)
                .exec();
        }
        
        for (DockerContainer cont : containers) {
            System.out.println("--- Start " + cont.getName() + " " + cont.getId());
            docker.startContainerCmd(cont.getId()).exec();
        }
	}
	
	@SuppressWarnings("resource")
    public void createNetwork() {
	    
	    String nameNet = (prefix + "net").replace("-", "_");
	    if (useExistingNetwork) {
	        for (Network network : docker.listNetworksCmd().exec()) {
	            // System.out.println("Net: " + network.getName() + " " + nameNet);
	            if (network.getName().equals(nameNet)) {
	                networkId = network.getId();
	                return;
	            }
	        }
	    }
	    
        System.out.println("--- CreateNetwork " + nameNet);

        Network.Ipam ipam = new Network.Ipam().withConfig(new Network.Ipam.Config()
                .withSubnet("10.67.79.0/24")
                .withGateway("10.67.79.1"));

        HashMap<String,String> optionsNet = new HashMap<>();
        optionsNet.put("com.docker.network.bridge.name", nameNet);
        optionsNet.put("com.docker.network.bridge.host_binding_ipv4", "0.0.0.0");
        optionsNet.put("com.docker.network.bridge.enable_icc", "true");
        optionsNet.put("com.docker.network.bridge.enable_ip_masquerade", "true");
        optionsNet.put("com.docker.network.driver.mtu", "1500");
        
        CreateNetworkResponse resNet = docker.createNetworkCmd()
                .withName(nameNet)
//              .withDriver("bridge")
//              .withOptions(optionsNet)
                .withIpam(ipam)
                .exec();
        if (resNet.getWarnings() != null)
            for (String warning : resNet.getWarnings())
                System.out.println("    " + warning);
        networkId = resNet.getId();
        
    }

    public void destroyNetwork(boolean forced) {
        
        if (useExistingNetwork && !forced) 
            return;
        
        init();
        if (networkId != null) {
            try {
                docker.removeNetworkCmd(networkId).exec();
            } catch (Exception e) {}
            networkId = null;
        }
        String nameNet = (prefix + "net").replace("-", "_");
        for (Network network : docker.listNetworksCmd().exec()) {
            // System.out.println("Net: " + network.getName() + " " + nameNet);
            if (network.getName().equals(nameNet)) {
                networkId = network.getId();
                break;
            }
        }
        if (networkId != null) {
            try {
                docker.removeNetworkCmd(networkId).exec();
            } catch (Exception e) {}
            networkId = null;
        }
    }

    /**
	 * Stop and remove all containers starting with the prefix.
	 */
	public void destroyPrefix() {
		init();
		
		ArrayList<String> remove = new ArrayList<>();
		for (Container cont : docker.listContainersCmd().withShowAll(true).exec()) {
			String removeName = null;
			for (String name : cont.getNames())
				if (name.startsWith("/" + prefix)) {
					removeName = name;
					break;
				}
			if (removeName == null) continue;
			
			System.out.println("--- Stop " + removeName);
			remove.add(cont.getId());
			try {
		        docker.stopContainerCmd(cont.getId()).withTimeout(60).exec();
			} catch (NotModifiedException e) {}
			
		}

		for (String id : remove) {
			System.out.println("--- Remove " + id);
			docker.removeContainerCmd(id).exec();
		}

		for (DockerContainer cont2 : containers)
			cont2.setId(this, null);
		
        destroyNetwork(false);
	}
	
	/**
	 * Stop end remove the configured containers.
	 */
	public void destroy() {
		init();
		fetchContainers();
		
        ArrayList<DockerContainer> reverse = new ArrayList<>(containers);
        ArrayList<String> remove = new ArrayList<>();
        Collections.reverse(reverse);
        
		for (DockerContainer cont : reverse) {
			if (cont.getId() != null) {
				System.out.println("--- Stop " + cont.getName());
				remove.add(cont.getId());
				try {
				    if (cont.isRunning()) {
				        docker.stopContainerCmd(cont.getId()).withTimeout(60).exec();
				    }
				} catch (NotModifiedException e) {}
			}
		}

        for (DockerContainer cont : reverse) {
            while (cont.isRunning()) {
                System.out.println("--- Running " + cont.getName());
                MThread.sleep(200);
            }
            System.out.println("--- Done " + cont.getName());
            cont.setId(this, null);
        }
		for (String id : remove) {
			System.out.println("--- Remove " + id);
			docker.removeContainerCmd(id).exec();
		}
		
	}
	
	public LogStream exec1(String name, String ... cmd) throws InterruptedException, NotFoundException {
		return exec(name, cmd, null, false, null, null, null);
	}
	
	public LogStream exec(String name, String cmd) throws InterruptedException, NotFoundException {
		return exec(name, cmd.split(" "), null, false, null, null, null);
	}
	
	public LogStream exec(String name, String cmd, String send) throws InterruptedException, NotFoundException {
		return exec(name, cmd.split(" "), null, false, null, null, send);
	}
	
    public LogStream exec(String name, String[] cmd, List<String> env, boolean privileges, String user, String dir, String send) throws InterruptedException, NotFoundException {
        DockerContainer cont = get(name);
        if (cont.getId() == null) throw new NotFoundException("Container not started",cont.getName());
        
        LogStream stream = new LogStream(cont);
        
        return exec(stream, cmd, env, privileges, user, dir, send);
    }
    
	public LogStream exec(LogStream stream, String[] cmd, List<String> env, boolean privileges, String user, String dir, String send) throws InterruptedException, NotFoundException {

	    DockerContainer cont = stream.getContainer();
	    
		ExecCreateCmd builder = docker.execCreateCmd(cont.getId())
			.withAttachStderr(true)
			.withAttachStdout(true)
			.withTty(true);
		
		builder.withCmd(cmd);
		if (send != null)
			builder.withAttachStdin(true);
		if (env != null)
			builder.withEnv(env);
		if (privileges)
			builder.withPrivileged(true);
		if (user != null)
			builder.withUser(user);
		if (dir != null)
			builder.withWorkingDir(dir);
		
		ExecCreateCmdResponse create = builder.exec();
		
		ExecStartCmd startBuilder = docker.execStartCmd(create.getId());
		if (send != null) {
			ByteArrayInputStream is = new ByteArrayInputStream(send.getBytes(MString.CHARSET_CHARSET_UTF_8));
			startBuilder.withStdIn(is);
		}
		
		startBuilder.exec(stream);
		
		return stream;
	}
	
	public LogStream attach(String name, String send) throws InterruptedException, NotFoundException {
		DockerContainer cont = get(name);
		if (cont.getId() == null) throw new NotFoundException("Container not started",cont.getName());
		
		LogStream stream = new LogStream(cont);
		
		return attach(stream, send);
	}

    public LogStream attach(LogStream stream, String send) throws InterruptedException, NotFoundException {

        DockerContainer cont = stream.getContainer();
        
        AttachContainerCmd builder = docker.attachContainerCmd(cont.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true);
        
        if (send != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(send.getBytes(MString.CHARSET_CHARSET_UTF_8));
            builder.withStdIn(is);
        }
        
        builder.exec(stream);
        stream.awaitStarted(60, TimeUnit.SECONDS);
        return stream;
    }
	
    public LogStream logs(String name, boolean follow, int tail) throws NotFoundException {
        DockerContainer cont = get(name);
        if (cont.getId() == null) throw new NotFoundException("Container not started",cont.getName());
        
        LogStream stream =  new LogStream(cont);
        
        return logs(stream, follow, tail);
    }
    
	public LogStream logs(LogStream stream, boolean follow, int tail) throws NotFoundException {
	    DockerContainer cont = stream.getContainer();
		LogContainerCmd lcc = docker.logContainerCmd(cont.getId());
		lcc.withStdErr(true);
		lcc.withStdOut(true);
		lcc.withFollowStream(follow);
		if (tail > 0)
		    lcc.withTail(tail);
		lcc.exec(stream);
		return stream;
	}
	
    public void waitForLogEntry(String name, String waitForString, int tail) throws NotFoundException, IOException, InterruptedException {
        try (LogStream logStream = logs(name, true, tail)) {
            logStream.awaitStarted(60, TimeUnit.SECONDS);
            waitForLogEntry(logStream,  waitForString);
        }
    }

	public void waitForLogEntry(LogStream logStream, String waitForString) throws NotFoundException, IOException {

        WaitContainer waitCont = new WaitContainer();
        waitCont.cont = logStream.getContainer();
        if (waitCont.cont.getId() == null) throw new NotFoundException("Container not started",waitCont.cont.getName());

        watch.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    if (!waitCont.running) {
                        cancel();
                    } else
                    if (!waitCont.cont.isRunning()) {
                        System.err.println("#### CLOSE " + waitCont.cont.getName() + " ####");
                        logStream.close();
                        cancel();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            
        }, 1000, 1000);
        
        try {
            while (true) {

                String logStr = logStream.readLine();
//                System.err.println(logStr);
                if (logStr.contains(waitForString)) {
                    return;
                }
                if (logStream.isClosed())
                    throw new EOFException();
            }
        } finally {
            waitCont.running = false;
        }
    
	}

	private static class WaitContainer {
        public DockerContainer cont;
        volatile boolean running = true;

	}
	
	private void fetchContainers() {
		
		for (DockerContainer cont2 : containers)
			cont2.setId(this, null);
		
		for (Container cont : docker.listContainersCmd().withShowAll(true).exec()) {
//			System.out.println("Existing " + Arrays.toString(cont.getNames()));
			for (DockerContainer cont2 : containers) {
				if (MCollection.contains(cont.getNames(),"/" + prefix + cont2.getName()))
					cont2.setId(this, cont.getId());
			}
		}
		
	}

	public String getPrefix() {
		return prefix;
	}

    public DockerClient getClient() {
        return docker;
    }

    public boolean isUseExistingNetwork() {
        return useExistingNetwork;
    }

    public DockerScenario setUseExistingNetwork(boolean useExistingNetwork) {
        this.useExistingNetwork = useExistingNetwork;
        return this;
    }

    String getExternalHost() {
        if (System.getenv("DOCKER_HOST") == null) {
            return "localhost";
        }
        return System.getenv("DOCKER_HOST");
    }
	
}
