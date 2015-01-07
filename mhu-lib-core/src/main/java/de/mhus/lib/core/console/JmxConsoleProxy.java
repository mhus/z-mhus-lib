package de.mhus.lib.core.console;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.jmx.MJmx;

@JmxManaged(descrition="Virtual console management interface")
public class JmxConsoleProxy extends MJmx {

	private JmxConsole console;

	public JmxConsoleProxy(JmxConsole jmxConsole) {
		console = jmxConsole;
	}

	@JmxManaged(descrition="Simulate typing")
	public void print(String in) {
		console.getInputWriter().print(in);
	}

	@JmxManaged(descrition="Simulate typing with ENTER at the end")
	public void println(String in) {
		console.getInputWriter().println(in);
	}

	@JmxManaged(descrition="Show the monochome display")
	public String getDisplay() {
		return console.getMonoDisplayAsString();
	}

	@JmxManaged(descrition="Resize the display, Parameters: width, height")
	public void resize(int width, int height) {
		console.resize(width, height);
	}
}
