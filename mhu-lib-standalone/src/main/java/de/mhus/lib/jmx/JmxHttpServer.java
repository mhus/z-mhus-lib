package de.mhus.lib.jmx;

import javax.management.ObjectName;

import com.sun.jdmk.comm.AuthInfo;
import com.sun.jdmk.comm.HtmlAdaptorServer;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.jmx.MRemoteManager;
import de.mhus.lib.errors.MException;

@JmxManaged(descrition = "Jmx Http Server")
public class JmxHttpServer extends MJmx {

	private HtmlAdaptorServer server;
	private IConfig config;

	public void setConfig(IConfig config) {
		this.config = config;
	}
	
	@JmxManaged
	public void openServer() throws MException {
		if (config == null) { // auto load config
			config = MSingleton.getCfg(this);
		}
		if (config == null || server != null) return;
		server = new HtmlAdaptorServer(config.getInt("port", 1098));
		
		for (IConfig cuser : config.getNodes("user")) {
			server.addUserAuthenticationInfo(new AuthInfo(cuser.getExtracted("name"), MPassword.decode(cuser.getExtracted("password")) ));
		}
		
		// TODO load user auth infos into server
		//server.setMBeanServer(mbs);
		try {
			MSingleton.lookup(MRemoteManager.class).register(new ObjectName("adaptor:proptocol=HTTP"),server,false,false);
//			mbs.registerMBean(server, new ObjectName("adaptor:proptocol=HTTP"));
			server.setMBeanServer(MSingleton.lookup(MRemoteManager.class).getMBeanServer());
		} catch (Exception e) {
			log().w(e);
			server = null;
			return;
		}
	    server.start();
	}
	
	@JmxManaged
	public void closeServer() {
		if (server == null) return;
		server.stop();
		server = null;
	}

}
