package de.mhus.lib.core.system;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.LogFactory;

/**
 * <p>DefaultSingleton class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultSingleton implements ISingleton, SingletonInitialize, ISingletonInternal {
	
	private LogFactory logFactory = new ConsoleFactory();
	private BaseControl baseControl;
	private CfgManager configProvider;
	private HashSet<String> logTrace = new HashSet<>();
	private File baseDir = new File(".");

	/** {@inheritDoc} */
	@Override
	public void doInitialize(ClassLoader coreLoader) {
		getCfgManager().reConfigure();
	}
		
	/** {@inheritDoc} */
	@Override
	public synchronized BaseControl getBaseControl() {
		if (baseControl == null) {
			baseControl = new BaseControl();
		}
		return baseControl;
	}

	/** {@inheritDoc} */
	@Override
	public MActivator createActivator() {
		return new DefaultActivator();
	}

	/** {@inheritDoc} */
	@Override
	public LogFactory getLogFactory() {
		return logFactory;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized CfgManager getCfgManager() {
		if (configProvider == null) {
			configProvider = new CfgManager(this);
		}
		return configProvider;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTrace(String name) {
		return logTrace.contains(name);
	}

	/** {@inheritDoc} */
	@Override
	public Base base() {
		return getBaseControl().getCurrentBase();
	}

	/** {@inheritDoc} */
	@Override
	public void setLogFactory(LogFactory logFactory) {
		this.logFactory = logFactory;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> getLogTrace() {
		return logTrace;
	}

	/** {@inheritDoc} */
	@Override
	public void setBaseDir(File file) {
		baseDir  = file;
		baseDir.mkdirs();
	}

	/** {@inheritDoc} */
	@Override
	public File getFile(String dir) {
		return new File(baseDir, dir);
	}

	/** {@inheritDoc} */
	@Override
	public String getSystemProperty(String name, String def) {
		return System.getProperty(name, def);
	}
	
}
