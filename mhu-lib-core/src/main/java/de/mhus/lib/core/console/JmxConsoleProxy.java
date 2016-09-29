package de.mhus.lib.core.console;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.jmx.MJmx;

/**
 * <p>JmxConsoleProxy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition="Virtual console management interface")
public class JmxConsoleProxy extends MJmx {

	private JmxConsole console;

	/**
	 * <p>Constructor for JmxConsoleProxy.</p>
	 *
	 * @param jmxConsole a {@link de.mhus.lib.core.console.JmxConsole} object.
	 */
	public JmxConsoleProxy(JmxConsole jmxConsole) {
		console = jmxConsole;
	}

	/**
	 * <p>print.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition="Simulate typing")
	public void print(String in) {
		console.getInputWriter().print(in);
	}

	/**
	 * <p>println.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition="Simulate typing with ENTER at the end")
	public void println(String in) {
		console.getInputWriter().println(in);
	}

	/**
	 * <p>getDisplay.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@JmxManaged(descrition="Show the monochome display")
	public String getDisplay() {
		return console.getMonoDisplayAsString();
	}

	/**
	 * <p>resize.</p>
	 *
	 * @param width a int.
	 * @param height a int.
	 */
	@JmxManaged(descrition="Resize the display, Parameters: width, height")
	public void resize(int width, int height) {
		console.resize(width, height);
	}
}
