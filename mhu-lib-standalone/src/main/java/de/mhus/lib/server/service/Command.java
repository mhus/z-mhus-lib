package de.mhus.lib.server.service;

import java.util.Map;

import de.mhus.lib.server.Task;

public class Command extends Task {

	@Override
	public void pass() throws Exception {
		for (int i = 0; options.isProperty("cmd" + i); i++) {
			String cmd = options.getString("cmd" + i,null);
			execute(cmd, i);
		}
	}

	private void execute(String cmd, int i) {
		if (cmd.equals("objects.clear")) {
			base.objects().clear();
		} else
		if (cmd.equals("options.clear")) {
			base.getOptions().clear();
		} else
		if (cmd.equals("objects.print")) {
			for (Map.Entry<String,Object> o : base.objects().entrySet())
				System.out.println(o.getKey() + "=" + o.getValue());
		}
	}

}
