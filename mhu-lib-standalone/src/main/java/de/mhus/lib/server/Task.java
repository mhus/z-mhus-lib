package de.mhus.lib.server;

import java.util.Map;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MStopWatch;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.jmx.MJmx;

public abstract class Task extends MJmx {

	protected ResourceNode config;
	protected TaskConfig base;
	protected MProperties options;

	public static void main(String[] args) throws Exception {
		String taskName = Thread.currentThread().getStackTrace()[1].getClassName();
		Main.main(new String[] {taskName});
	}
	
	public boolean initAndRun() throws Exception {
		TaskConfig initializer = new TaskConfig();
		initializer.init();
		init(initializer);
		return run();
	}
	
	public boolean run() {
		
		if (options == null)
			options = new MProperties();
		
		if (base.getOptions() != null)
			for (Map.Entry<String, String> entry : base.getOptions().entrySet()) {
				if (!options.isProperty(entry.getKey()))
					options.setProperty(entry.getKey(), entry.getValue());
			}
		
		boolean ret = true;
		log().i("-----------------------------------------------");
		log().i(">>> EXECUTE", getClass().getCanonicalName(), options);
//		MSingleton.instance().getConsole().println("EXECUTE: " + getClass().getCanonicalName());
		MStopWatch watch = new MStopWatch(getClass().getCanonicalName());
		watch.start();
		try {
			pass();
		} catch (Throwable t) {
			t.printStackTrace();
			ret = false;
		}
		watch.stop();
		log().i("-----------------------------------------------");
		log().i("TIME: " + watch.getCurrentTimeAsString());
//		MSingleton.instance().getConsole().println("TIME: " + getClass().getCanonicalName() + ": " + watch.getCurrentTimeAsString());
		return ret;
	}
	
	public void init(TaskConfig tc) {
		this.base = tc;
		
		this.config = base.config();
	}
	
	public abstract void pass() throws Exception;

	public void setOptions(Map<String, String> options) {
		this.options = new MProperties();
		if (options != null)
			for (Map.Entry<String, String> entry : options.entrySet())
				this.options.setProperty(entry.getKey(), entry.getValue());
	}
	
}
