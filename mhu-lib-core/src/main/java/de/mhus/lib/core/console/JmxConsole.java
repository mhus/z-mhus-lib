package de.mhus.lib.core.console;

import java.io.IOException;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.service.ConfigProvider;
import de.mhus.lib.errors.MException;

/**
 * <p>JmxConsole class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JmxConsole extends VirtualConsole {

	@SuppressWarnings("unused")
	private JmxConsoleProxy jmxProxy;

	/**
	 * <p>Constructor for JmxConsole.</p>
	 *
	 * @throws java.io.IOException if any.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public JmxConsole() throws IOException, MException {
		super();
		width = 80;
		height = 40;
		echo = true;
		ResourceNode config = MSingleton.get().getBaseControl().base(this).lookup(ConfigProvider.class).getConfig(this, null);
		if (config != null) {
			width = config.getInt("width",width);
			height = config.getInt("height", height);
		}
		reset();
		jmxProxy = new JmxConsoleProxy(this);
	}

}
