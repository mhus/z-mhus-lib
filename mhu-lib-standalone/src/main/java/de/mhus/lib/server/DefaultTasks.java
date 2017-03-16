package de.mhus.lib.server;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.server.service.Command;
import de.mhus.lib.server.service.GarbageCollectionTrigger;
import de.mhus.lib.server.service.Quit;
import de.mhus.lib.server.service.Reset;
import de.mhus.lib.server.service.Set;

public class DefaultTasks extends MObject {

	@SuppressWarnings("unchecked")
	public DefaultTasks() {
		Main main = MApi.lookup(Main.class);
		main.appendList(new TaskListDefinition("q","Quit", new Class[] {
				Quit.class
		}));
		
		main.appendList(new TaskListDefinition("r","Reset", new Class[] {
				Reset.class
		}));
		
		main.appendList(new TaskListDefinition("gc","Garbage Collection", new Class[] {
				GarbageCollectionTrigger.class
		}));
		
		main.appendList(new TaskListDefinition("set","Set Options", new Class[] {
				Set.class
		}));

		main.appendList(new TaskListDefinition("cmd","Commands ... cmd0=objects.clear&cmd1=options.clear", new Class[] {
				Command.class
		}));
		
	}
}
