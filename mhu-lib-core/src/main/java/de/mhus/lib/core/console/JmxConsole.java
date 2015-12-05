package de.mhus.lib.core.console;

import java.io.IOException;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.system.CfgManager;
import de.mhus.lib.errors.MException;

public class JmxConsole extends VirtualConsole {

	@SuppressWarnings("unused")
	private JmxConsoleProxy jmxProxy;

	public JmxConsole() throws IOException, MException {
		super();
		width = 80;
		height = 40;
		echo = true;
		ResourceNode config = MSingleton.get().getBaseControl().base(this).lookup(CfgManager.class).getCfg(this, null);
		if (config != null) {
			width = config.getInt("width",width);
			height = config.getInt("height", height);
		}
		reset();
		jmxProxy = new JmxConsoleProxy(this);
	}

}
