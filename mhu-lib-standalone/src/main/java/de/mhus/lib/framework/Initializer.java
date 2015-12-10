package de.mhus.lib.framework;

import java.lang.reflect.Method;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.jmx.MJmx;

@JmxManaged(descrition="Initializer management interface")
public class Initializer extends MJmx {

	private IConfig config;
	private MActivator base;

	public Initializer(IConfig config, MActivator base) {
		this.config = config;
		this.base = base;
	}
	
	public void initialize() {
		
		if (base instanceof MutableActivator) {
			((MutableActivator)base).addObject(IConfig.class,null,config);
			((MutableActivator)base).addObject(Initializer.class,null,this);
		}
		
		ResourceNode cInit = config.getNode("initialize");
		for (ResourceNode cStart : cInit.getNodes("start")) {
			try {
				String clazz = cStart.getExtracted("class");
				String interf = cStart.getExtracted("interface");
				String method = cStart.getExtracted("method");
				start(clazz, interf,method);
			} catch (Throwable t) {
				log().e(t);
			}
		}
		
		jmxRegister(true,getClass().getCanonicalName());
	}
	
	@JmxManaged(descrition="Call System.exit, give the secret from configuration for security. p1=return code, p2=secret")
	public void exit(int rc,String secret) {
		if (!hasSuperPrivileges(secret)) return;
		log().i("Exit by user request via jmx");
		System.exit(rc);
	}

	private boolean hasSuperPrivileges(String secret) {
		if (secret == null) return false;
			return secret.equals(MPassword.decode(config.getString("secret", null)));
	}

	@JmxManaged(descrition="This is a simple helpfull function to encode a password on the running system (see MPassword). p1=encoding algorithm, p2=password to encode")
	public String encodePassword(int method, String in) {
		return MPassword.encode(method, in);
	}
	
	@JmxManaged(descrition="Start a service, p1=class, p2=interface, p3=method")
	public void start(String clazz, String interf, String method ) throws Exception {
		log().i("start",clazz);
		Object obj = base.getObject(clazz);
		if (interf != null && base instanceof MutableActivator) {
			log().i("interface",interf);
			((MutableActivator)base).addObject(null,interf,obj);
		}
		if (MString.isSet(method)) {
			log().i("method",method);
			Method me = obj.getClass().getMethod(method, new Class<?>[0]);
			me.invoke(obj, new Object[0]);
		}
		log().i("finished",clazz);
	}

	@JmxManaged
	public String[] getMapNames() {
		if (!(base instanceof MutableActivator)) return null;
		return ((MutableActivator)base).getMapNames();
	}
	
	@JmxManaged
	public String[] getObjectNames() {
		if (!(base instanceof MutableActivator)) return null;
		return ((MutableActivator)base).getObjectNames();
	}
	
}
