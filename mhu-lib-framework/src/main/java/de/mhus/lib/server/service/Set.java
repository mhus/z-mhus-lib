package de.mhus.lib.server.service;

import java.util.Map.Entry;

import de.mhus.lib.server.Task;

public class Set extends Task {

	@Override
	public void pass() throws Exception {
		
		if (options.getBoolean("__objclear", false)) {
			log().i("objects clear");
			base.objects().clear();
		}
		if (options.getBoolean("__optclear", false)) {
			log().i("options clear");
			base.getOptions().clear();
		}
		
		for (Entry<String,Object> entry : options) {
			log().i(entry.getKey(),String.valueOf(entry.getValue()));
			base.getOptions().put(entry.getKey(),String.valueOf(entry.getValue()));
		}

		log().i("print",base.getOptions());

	}

}
