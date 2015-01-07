package de.mhus.lib.server.service;

import de.mhus.lib.server.Task;



public class Reset extends Task {

	@Override
	public void pass() throws Exception {
		log().i("Reset by user request");
//		watch.stop();
//		System.out.println("TOTAL TIME: " + watch.getCurrentTimeAsString(true));
		System.exit(1);
	}

}
