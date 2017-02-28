package de.mhus.lib.server;


import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MStopWatch;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.console.Console;
import de.mhus.lib.core.io.CSVReader;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.system.DefaultBase;
import de.mhus.lib.core.util.MUri;

@JmxManaged
public class Main extends MJmx {

	private String actionDate = MDate.toIsoDate(new Date());

	//	private Initializer initializer;
	private String status;
	TreeMap<String, TaskListDefinition> lists;

	private TaskConfig initializer;

	public static void main(String[] args) throws Exception {
//		Standalone.initialize(args, "config.xml");
		new Main().run(args);
	}
	
	public void run(String[] args) throws Exception {

		boolean exitOnEnd = false;
		
		((MutableActivator)((DefaultBase)MSingleton.get().getBaseControl().base()).getActivator()).addObject(Main.class, null, this);
		lists = new TreeMap<String, TaskListDefinition>();
		
		new de.mhus.lib.framework.Initializer(new XmlConfigFile(new File("config.xml")), ((DefaultBase)MSingleton.get().getBaseControl().base()).getActivator() ).initialize();
		
		Console console = MSingleton.lookup(Console.class);
		while (true) {
			// menu 
			//console.printLine();
			status = "[sleeping]";
//			int cnt = 0;
//			console.println("gc: Garbage Collection");
//			console.println("q: Quit");
//			console.println("r: Reload");
			console.println("reset: reset execution context");
			for (String name : lists.keySet()) {
				console.println(lists.get(name));
			}
			
			if (initializer != null) {
				console.println("env=" + initializer.getOptions());
				console.println("obj=" + initializer.objects());
			}
			
			console.print("> ");
			String line = null;
			

			if (args.length > 0) {
				exitOnEnd = true;
				line = MString.join(args, ' ');
				args = new String[0];
			} else {
				line = console.readLine();
				line = line.replace('\n', ' ');
				line = line.replace('\r', ' ');
				line = line.replace('\t', ' ');
				line = line.trim();
			}
			console.println(">>> Execute: " + line);
			
			// INIT 
//			((Standalone)MSingleton.instance()).initConfig(null); // reload
			if (initializer == null) {
				resetInitializer();
			}
			
			// END INIT
			
			if (line.startsWith(">optfile")) {
				line = MString.afterIndex(line, ' ');
				String file = MString.beforeIndex(line, ' ');
				line = MString.afterIndex(line, ' ');
				
				FileReader r = new FileReader(new File(file));
				CSVReader csv = new CSVReader(r);
				try {
					while (true) {
						String[] x = csv.getAllFieldsInLine();
						if (x.length > 1) {
							System.out.println(">>> " + x[0] + "=" + x[1]);
							HashMap<String, String> defOptions = new HashMap<String, String>();
							for (int i = 0; i < x.length; i+=2)
								defOptions.put(x[i], x[i+1]);
							
							try {
								execute(line, defOptions);
							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
				r.close();
				
			} else
				execute(line, null);
				
			if (exitOnEnd)
				return;
		}

	}

	private void resetInitializer() throws Exception {
		log().i("reset context");
		initializer = new TaskConfig();
		initializer.init();
	}

	@SuppressWarnings("unchecked")
	private void execute(String line, Map<String,String> defOptions) {

		actionDate = MDate.toIsoDate(new Date());

		if ( !MString.isEmpty(line)) {
			String[] parts = MString.split(line, " ");
			
			MStopWatch watch = new MStopWatch("total");
			watch.start();

			try {
				for (String part : parts) {
					
					if (MString.isSetTrim(part)) {
						
						part = part.trim();
						
						// have options ?
						String optionsString = null;
						Map<String,String> options = null;
						{
							int p = part.indexOf(',');
							if (p > 0) {
								optionsString = part.substring(p+1);
								part = part.substring(0,p);
							}
							if (optionsString != null) {
								optionsString = optionsString.trim();
								options = MUri.explode(optionsString);
							}
							
						}
						
						Class<? extends Task>[] commands = null;
						
						if (part.equals("reset")) {
							resetInitializer();
						} else
						if (lists.containsKey(part)) {
							commands = lists.get(part).getTasks();
						} else {
							
							Class<? extends Task> command = null;
							try {
								command = (Class<? extends Task>) getClass().getClassLoader().loadClass(part);
								commands = new Class[] { command };
							} catch (ClassNotFoundException cnfe) {
							}
							
						}
						
						if (commands != null) {
							for (Class<? extends Task> command : commands)
								if (!executePass(initializer, command, options)) break;
						} else
							System.out.println("Command not found: " + part);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				watch.stop();
				System.out.println("TOTAL TIME: " + watch.getCurrentTimeAsString());
			}
		}
	}
	
	private boolean executePass(TaskConfig config, Class<?> command, Map<String, String> options) throws Exception {
		Task p = create(config, command);
		p.setOptions(options);
		status = p.getClass().getName();
		return p.run();
	}

	private Task create(TaskConfig initializer, Class<?> clazz) throws Exception {
//		Task p = (Task) base(clazz); // TODO new Instance !!
		Task p = (Task) clazz.newInstance();
		p.init(initializer);
		return p;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JmxManaged
	public String getStatus() {
		return status;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void appendList(TaskListDefinition taskList) {
		lists.put(taskList.getName(), taskList);
	}

//	System.exit(0);
}
